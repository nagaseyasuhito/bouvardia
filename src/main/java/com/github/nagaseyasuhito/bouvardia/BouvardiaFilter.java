package com.github.nagaseyasuhito.bouvardia;

import javax.servlet.annotation.WebFilter;

import com.google.inject.servlet.GuiceFilter;

@WebFilter("/*")
public class BouvardiaFilter extends GuiceFilter {
}
