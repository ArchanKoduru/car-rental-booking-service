package com.xyz.carrental.booking;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class WireMockSetup {
    public static WireMockServer wireMockServer;

    @BeforeAll
    public static void startWiremock() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().port(9090));
        wireMockServer.start();
    }

    @AfterAll
    public static void stopWiremock() {
        if (wireMockServer != null) wireMockServer.stop();
    }
}
