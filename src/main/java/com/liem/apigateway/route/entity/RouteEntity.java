package com.liem.apigateway.route.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotBlank;

@Table("routes")
@Data
@Builder
public class RouteEntity {

    @Id
    private Long id;

    @NotBlank
    @Column("uri")
    private String uri;

    @NotBlank
    @Column("service_name")
    private String serviceName;

    @NotBlank
    @Column("path")
    private String path;

    @Column("is_auth")
    private boolean auth;

    @Column("ia_active")
    private boolean active;
}
