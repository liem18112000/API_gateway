package com.liem.apigateway.route.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
public class RouteDTO implements Serializable {
    public static final long serialVersionUID = 780145961842519590L;

    protected Long id;

    protected String serviceName;

    protected String uri;

    protected String path;

    protected boolean auth;

    protected boolean active;
}
