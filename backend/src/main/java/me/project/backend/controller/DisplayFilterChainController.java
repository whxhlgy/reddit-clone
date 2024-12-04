package me.project.backend.controller;

import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DisplayFilterChainController {
    @Autowired
    private FilterChainProxy filterChainProxy;

    @RequestMapping("/filterChain")
    public @ResponseBody Map<Integer, Map<Integer, String>> getSecurityFilterChainProxy(){
        return this.getSecurityFilterChainProxyHelper();
    }

    public Map<Integer, Map<Integer, String>> getSecurityFilterChainProxyHelper(){
        Map<Integer, Map<Integer, String>> filterChains= new HashMap<Integer, Map<Integer, String>>();
        int i = 1;
        for(SecurityFilterChain secfc :  this.filterChainProxy.getFilterChains()){
            //filters.put(i++, secfc.getClass().getName());
            Map<Integer, String> filters = new HashMap<Integer, String>();
            int j = 1;
            for(Filter filter : secfc.getFilters()){
                filters.put(j++, filter.getClass().getName());
            }
            filterChains.put(i++, filters);
        }
        return filterChains;
    }
}
