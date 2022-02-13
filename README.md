# bootjwt

How to secure backend applications using JWT, Spring Boot and Spring Security
Spring Boot and Spring Security with JWT including Access and Refresh Tokens




		Client										Application
		
			->  POST/login username and password ->
			
			<-   JSON Web Token	(JWT)			 <-
			
			->  GET/users (JWT in Header)		 ->