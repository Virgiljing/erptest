package cn.itcast.erp.realm;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

public class MyAuthorizationFilter extends PermissionsAuthorizationFilter{
	@Override
	public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws IOException {
		Subject subject = getSubject(request, response);
		String[] perms = (String[])mappedValue;
		if (null == perms) {
			return true;
		}
		for (String perm : perms) {
			if (subject.isPermitted(perm)) {
				return true;
			}
		}
		return false;
	}
}
