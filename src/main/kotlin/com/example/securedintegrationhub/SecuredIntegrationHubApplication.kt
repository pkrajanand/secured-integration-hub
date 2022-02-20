package com.example.securedintegrationhub

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
class SecuredIntegrationHubApplication: WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        with(http) {
            with(authorizeRequests()) {
                antMatchers("/", "/error", "/webjars/**").permitAll()
                anyRequest().authenticated()
            }
            with(exceptionHandling()) {
                authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            }
            oauth2Login()
            with(logout()) {
                logoutSuccessUrl("/").permitAll()
            }
            with(csrf()) {
                csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            }

        }
    }

    @GetMapping("/user")
    fun user(@AuthenticationPrincipal principal: OAuth2User): Map<String?, Any?>? {
        return mapOf(Pair("name", principal.getAttribute("login")))
    }
}

fun main(args: Array<String>) {
    runApplication<SecuredIntegrationHubApplication>(*args)
}
