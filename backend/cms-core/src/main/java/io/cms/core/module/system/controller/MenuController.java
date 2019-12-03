package io.cms.core.module.system.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.cms.core.configure.annotation.LogOperation;
import io.cms.core.datasource.annotation.DataSource;
import io.cms.core.datasource.annotation.DataSource.DataSources;
import io.cms.core.module.system.service.interfaces.IMenuService;
import io.cms.core.module.system.vo.IMenu;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/menu")
@Api(value = "/menu", tags = "MenuController")
public class MenuController {

	@Autowired
	IMenuService menuService;

	/**
	 * 根据用户角色获取菜单列表
	 * 
	 * @param view
	 * @return
	 */
	@PostMapping("list")
	@PreAuthorize("hasAnyAuthority('MENU','MENU_LIST')")
	@DataSource(DataSources.PRIMARY)
	@ApiOperation(value = "根据用户 角色获取菜单列表", notes = "头部需要带token信息")
	public ResponseEntity<Collection<IMenu>> list(
			@RequestBody(required = false) @ApiParam(name = "object", value = "传入json格式", required = false) IMenu view,
			Principal principal) {
		if (view == null || StringUtils.isEmpty(view.getSearch())) {
			return new ResponseEntity<Collection<IMenu>>(menuService.query(view, true), HttpStatus.OK);
		} else {
			return new ResponseEntity<Collection<IMenu>>(menuService.query(view, false), HttpStatus.OK);
		}
	}

	@PostMapping("/save")
	@LogOperation("save")
	@PreAuthorize("hasAnyAuthority('USER','USER_SAVE')")
	@ApiOperation(value = "save")
	public ResponseEntity<IMenu> save(@RequestBody IMenu view) {
		return new ResponseEntity<IMenu>(menuService.saveOne(view), HttpStatus.OK);
	}

	@PutMapping("/save")
	@LogOperation("update")
	@PreAuthorize("hasAnyAuthority('USER','USER_SAVE')")
	@ApiOperation(value = "update")
	public ResponseEntity<IMenu> update(@RequestBody IMenu view) {
		return new ResponseEntity<IMenu>(menuService.saveOne(view), HttpStatus.OK);
	}

	@DeleteMapping("/delete")
	@LogOperation("delete")
	@PreAuthorize("hasAnyAuthority('MENU','MENU_DELETE')")
	@ApiOperation(value = "delete")
	public ResponseEntity<Object> logicDelete(@RequestParam(name = "ids") Collection<Integer> ids) {
		menuService.logicDelete(ids);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/download")
	@LogOperation("download")
	@PreAuthorize("hasAnyAuthority('MENU')")
	@ApiOperation(value = "download")
	public ResponseEntity<Object> download(@RequestBody Collection<Integer> ids, HttpServletResponse response)
			throws IOException {
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename=MenuTemplate.xls");
		response.setCharacterEncoding("UTF-8");
		OutputStream os = response.getOutputStream();
		Workbook workbook = new XSSFWorkbook(getClass().getResourceAsStream("/public/template/MenuTemplate.xlsx"));
		workbook.write(os);
		workbook.close();
		os.close();
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/upload")
	@LogOperation("upload")
	@PreAuthorize("hasAnyAuthority('MENU')")
	@ApiOperation(value = "upload")
	public ResponseEntity<Object> upload(MultipartFile[] file, HttpServletRequest req) throws IOException {
		System.out.println(file.length);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
