# SpringBootServerSample

Easy to use, and you just need to customize a few things to adapt to your business

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


