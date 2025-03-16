package com.mart.util;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;

@Component
public class JwtUtil {
	private final String SECRET_KEY = "fqKOx6t+zZhHZuo0NMzRo27dTBmx0JpMsIM4qnOhNQE=";

	public String generateToken(UserDetails userDetails) {
		return Jwts.builder().setSubject(userDetails.getUsername()).claim("roles", userDetails.getAuthorities())
				.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 giờ
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	public String extractUsername(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
	}

	public Date extractExpiration(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getExpiration();
	}

	public boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		try {
			String username = extractUsername(token);
			return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
		} catch (ExpiredJwtException e) {
			System.out.println("Token has expired: " + e.getMessage());
		} catch (UnsupportedJwtException e) {
			System.out.println("Unsupported token: " + e.getMessage());
		} catch (MalformedJwtException e) {
			System.out.println("Malformed token: " + e.getMessage());
		} catch (SignatureException e) {
			System.out.println("Invalid signature: " + e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println("Illegal argument token: " + e.getMessage());
		}
		return false;
	}
}
