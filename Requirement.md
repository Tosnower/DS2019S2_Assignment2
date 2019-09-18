# Requirement

Document.java: org.json.simple<br>
marven:<br>
```
<dependency>
<groupId>com.googlecode.json-simple</groupId>
<artifactId>json-simple</artifactId>
<version>1.1.1</version>
</dependency>
```


Construct the passing message method:
```
    public static Document Send_One(String text,String user) {
        Document request = new Document();
        request.append("command", "Send_One");
        request.append("text", text);
        request.append("user", user);
        return request;
    }
```

String like:
```send_string = Send_One(text,user).toJson;```


Server get message:
```
Document response = Document.parse(msg);
String command = response.getString("command");
switch (command) {
	case "Connect":
    	String loginUser =  response.getString("ip");
	...
}
```