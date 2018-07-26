package cn.itcast.erp.biz.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.itcast.erp.biz.IMenuBiz;
import cn.itcast.erp.dao.IMenuDao;
import cn.itcast.erp.entity.Menu;

/**
 * 菜单业务逻辑类
 *
 */
@Service("menuBiz")
public class MenuBiz extends BaseBiz<Menu> implements IMenuBiz {

    private IMenuDao menuDao;

    @Resource(name="menuDao")
    public void setMenuDao(IMenuDao menuDao) {
        this.menuDao = menuDao;
        super.setBaseDao(this.menuDao);
    }

	@Override
	public List<Menu> getMenusByEmpuuid(Long uuid) {
		
		return menuDao.getMenusByEmpuuid(uuid);
	}

	@Override
	public Menu readMenusByEmpuuid(Long uuid) {
		List<Menu> menusList = menuDao.getMenusByEmpuuid(uuid);
		Menu menu = menuDao.get("0");
		Menu _menu = copyMenu(menu);
		List<Menu> menus1 = menu.getMenus();
		for (Menu menu1 : menus1) {
			List<Menu> menus2 = menu1.getMenus();
			Menu _menu1 = copyMenu(menu1);
			for (Menu menu2 : menus2) {
				Menu _menu2 = copyMenu(menu2);
				if (null != menusList && menusList.contains(menu2)) {
					_menu1.getMenus().add(_menu2);
				}
			}
			if (_menu1.getMenus().size()>0) {
				_menu.getMenus().add(_menu1);
			}
		}
		return _menu;
	}
	
	private Menu copyMenu(Menu menu) {
		Menu _menu = new Menu();
		_menu.setIcon(menu.getIcon());
		_menu.setMenuid(menu.getMenuid());
		_menu.setMenuname(menu.getMenuname());
		_menu.setUrl(menu.getUrl());
		_menu.setMenus(new ArrayList<Menu>());
		return _menu;
	}
    
}
