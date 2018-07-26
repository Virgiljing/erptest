package cn.itcast.erp.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 角色实体类
 */
@Entity
@Table(name="role")
public class Role {
    @Id
    @GeneratedValue(generator="roleKeyGenerator",strategy=GenerationType.SEQUENCE)
    @GenericGenerator(name="roleKeyGenerator",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",
                parameters= {@Parameter(name="sequence_name",value="role_seq")}
            )
    private Long uuid;//编号
	private String name;//名称
	@ManyToMany(targetEntity=Menu.class)
	@JoinTable(name="role_menu",joinColumns= {
			@JoinColumn(name="roleuuid",referencedColumnName="uuid")
	},inverseJoinColumns= {
			@JoinColumn(name="menuuuid",referencedColumnName="menuid")
	})
	private List<Menu> menus;
	@ManyToMany(mappedBy="roles")
	private List<Emp> emps;
	public List<Emp> getEmps() {
		return emps;
	}
	public void setEmps(List<Emp> emps) {
		this.emps = emps;
	}
	public List<Menu> getMenus() {
		return menus;
	}
	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}
	public Long getUuid() {		
		return uuid;
	}
	public void setUuid(Long uuid) {
		this.uuid = uuid;
	}
	public String getName() {		
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
