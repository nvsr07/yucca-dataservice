package org.csi.yucca.adminapi.jwt;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloJWT
{

    @Autowired
    private JwtService jwtService;

    @GetMapping(value = "/api/secure/hello/{name}")
    public ResponseEntity<?> helloSecure(@PathVariable String name, HttpServletRequest request){

        JwtUser jwtUser = (JwtUser)request.getAttribute("jwtUser");
    	
        String result = String.format("Hello JWT, %s! (Secure)", name + " - " + jwtUser.getApplicationName());
        
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/api/public/hello/{name}")
    public ResponseEntity<?> helloPublic(@PathVariable String name){
    	
        String result = String.format("Hello JWT, %s! (Public)", name);
        
        return ResponseEntity.ok(result);
    }
    
}