package pomelo.util;

import java.util.HashMap;

import org.junit.Test;

import io.cms.util.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtTest {

	private JwtUtil jwtUtil = new JwtUtil(SignatureAlgorithm.HS512, "{{PomeloMan}}", 3600000l);

	@Test
	public void jwt() {
		String token = jwtUtil.generateToken("Subject", new HashMap<String, Object>());
		Claims c = jwtUtil.getClaimByToken(token);
		System.out.println(c.toString());
	}
}
