frog
====

a springmvc plugin, supported to list it`s RequestedMappings and test all the Actions.

usage:

1.package it and put into a spring-mvc project, and javassist.jar is required, see pom.xml.

2.add to &lt;context:component-scan base-package="com.zoo.frog"/&gt;


3.when the spring-mvc started, visit "http://ip:port/myproject/tracer-interface/list" to list requestmappings.<br>
eg: {"ilist":["/login2-{username}-[POST]","/login3[POST]","/login[GET]"]}

4.request "http://ip:port/myproject/tracer-interface/list?post_tracer_path=/login[GET]" to view the method sgin. <br>
eg: {"methodSign":"java.lang.String login1([] String username, [] String password)","paramNames":["username","password"]}

5.post to "http://localhost/tracer-interface/invoke" to invoke action.

<pre>
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
</pre>
