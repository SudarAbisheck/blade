package me.sudar.hello;

import blade.kit.json.JsonObject;

import com.blade.Blade;
import com.blade.route.RouteHandler;
import com.blade.web.http.Request;
import com.blade.web.http.Response;

public class App {
	
	public static void main(String... args){
		Blade blade = Blade.me();

        /**
         * JDK1.6
         */
        blade.get("/", new RouteHandler() {
            public void handle(Request request, Response response) {
                response.html("<h1>Hello Blade！</h1>");
            }
        });

        blade.setFavicon(System.getProperty("user.dir") + "/public/favicon.ico");
        // java8
        blade.get("/hello", (req,res) -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("name", "sudar");
            jsonObject.add("blog", "http://sudar.me");
            res.json(jsonObject.toString());
        });

        blade.before("/.*", (req,res) -> {
            System.out.println("before");
        });

        blade.listen(9004).start();
	}
}