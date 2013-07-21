frog
====

a spring-mvc plugin, supported to list it`s RequestedMappings and test all the Actions.

usage:

1.package it and put into a spring-mvc project, and javassist.jar is required, see pom.xml.

2.add configuare something, add it`s package scan config.
<context:component-scan base-package="xxx.xxx,com.zoo.frog"></context:component-scan>

3.when the spring-mvc started, visit http://ip:port/myproject/tracer-interface/list, list requestmappings.
eg: {"ilist":["/login2-{username}-[POST]","/login3[POST]","/login[GET]"]}

4.view http://ip:port/myproject/tracer-interface/list?post_tracer_path=/login[GET], view the method sgin. 
eg: {"methodSign":"java.lang.String login1([] String username, [] String password)","paramNames":["username","password"]}

5.visit http://localhost/tracer-interface/invoke to invoke action.

public static void main(String[] args) throws Exception {
		String web = "http://localhost/myproject/tracer-interface/invoke";
		URL url = new URL(web);
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		String param = "username=&post_tracer_path=/login3[POST]&password=admin";
		os.write(param.getBytes("utf-8"));
		os.flush();
		InputStream is = con.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String buf = br.readLine();
		while (buf != null) {
			System.out.println(buf);
			buf = br.readLine();
		}
		br.close();
		os.close();
	}

{"result":"test/login"}
