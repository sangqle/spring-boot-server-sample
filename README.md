# SpringBootServerSample
SpringBootServerSample is the sample server preresentation sample structure of project include Logging, Authentication, Configure datasource

Easy to use, and you just need to customize a few things to adapt to your business

# How to run project on your local

```bash
git clone https://github.com/sangqle/spring-boot-server-sample.git

cd spring-boot-server-sample

mvn clean install
```

## For VSCode
### One of the following ways:

- In main class at <code>SpringBootServerApplication.java</code> then <strong>right-click and select 'Run Java'</strong>
- Run with command line:
  ```bash
  mvn spring-boot:run
  ```
## For IDE
...

# Testing Application

Sample credentials that hardcode in the project you will use it to login in your application
```json
{
    "username": "admin",
    "password": "adminpass"
}
```

## Sample API
### Login
POST: http://localhost:8080/api/auth/login

Body:
```json
{
    "username": "admin",
    "password": "adminpass"
}
```

Sample response:
```json
{
    "msg": "Successful",
    "data": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY2OTY5NTIyNiwiZXhwIjoxNjcwMzAwMDI2fQ.lkJlMJu8M7HuYGkI0kyMGeAc7wMCQgLy0fh7yggCy2YXR8s5Xj0A580jRdB-WEGwQY_ZagDFyvqgyYK1Q74shw",
    "err": 0
}
```


### Testing sample controller

GET: http://localhost:8080/api/test/all

Headers:
```json
{
    ...
    "Authorization": "Bearer <Token from login>"
    ...
}
```

### Sample authorization

<code>TestController.java</code>
```java
public class TestController {

    @RolesAllowed({"ADMIN", "USER", "MOD"})
    @GetMapping("/all")
    public String allAccess() {
        return "All content here";
    }

    @GetMapping("/user")
    @RolesAllowed("USER")
    public String userAccess(Authentication authentication, Principal principal) {
        return "User Content.";
    }

    @GetMapping("/mod")
	@RolesAllowed("MOD")
	public String moderatorAccess() {
		return "Moderator Board.";
	}

	@GetMapping("/admin")
	@RolesAllowed("SUPER")
	public String adminaccess() {
		return "admin board.";
	}
}
```

# Project Structure

## 1. Authentication & Authorization via RestAPI

### 1.1 Authentication implement JWT solution base on Spring Security

Now, we can add the Spring Security framework to our project, and we can do this 
by adding the following dependency to our pom.xml
```xml
  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
  </dependencies>

```
#### AuthenticationManager
You can think of <code>AuthenticationManager</code> as a coordinator where you can register multiple providers, and based on the request type,
it will deliver an authentication request to the correct provider.


#### AuthenticationProvider
<code>AuthenticationProvider</code> processes specific types of authentication. Its interface exposes only two functions:

- <code>authenticate</code>: performs authentication with the request
- <code>supports</code>: checks if this provider supports the indicated authentication type

One import implementation of the interface that we are using in our sample project is <code>DaoAuthenticationProvider</code>, 
which retrieves user details from a <code>UserDetailsService</code>

In this tutorial we will use <code>CustomAuthenticationProvider</code> to authenticate username, password. It will be config in
 file <code>CustomAuthenticationProvider.java</code>
```java
public class CustomAuthenticationProvider implements AuthenticationProvider {
    public static Logger _Logger = LogManager.getLogger(CustomAuthenticationProvider.class);

    @Resource
    UserDetailsServiceImpl userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = (authentication.getPrincipal() == null) ? "" : authentication.getName();
        final String password = (authentication.getCredentials() == null) ? "" : (String) authentication.getCredentials();

        if (username.isEmpty() || password.isEmpty()) {
            throw new BadCredentialsException("invalid login details");
        }

        // get user details using Spring security user details service
        UserDetails user = null;
        try {
            user = userDetailsService.loadUserByUsername(username);
            if (user == null || !user.getPassword().equals(password)) {
                _Logger.error(String.format("Invalid credentials username: %s", username));
                return null;
            }
        } catch (Exception ex) {
            _Logger.error(ex.getMessage(), ex);
        }
        return createSuccessfulAuthentication(authentication, user);
    }

    private Authentication createSuccessfulAuthentication(final Authentication authentication, final UserDetails user) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                authentication.getCredentials(),
                user.getAuthorities()
        );

        token.setDetails(user);
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
```

#### Register Custom Authentication Provider

We will config in file <code>SecurityConfig.java</code>
```java
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // Another code be here
    @Bean
    public CustomAuthenticationProvider authProvider() {
        CustomAuthenticationProvider authenticationProvider = new CustomAuthenticationProvider();
        return authenticationProvider;
    }
    // Another code be here
}
```

#### UserDetailsService
<code>UserDetailsService</code> is describe as a core interface that load user-specific data in the Spring documentation.

In this project we will use <code>UserDetailsServiceImpl.java</code>. It will be implement some basic interface from <code>UserDetailsService</code>

```java
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Get user from your database we will hardcode for test
        if(username.equals("admin")) {
            return new UserDetailsImpl(1, "admin","admin@gmail.com", "adminpass", null);
        }
        return null;
    }
}
```


