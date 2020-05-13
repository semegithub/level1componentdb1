package edu.cloudnative.poc;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

@Liveness
@ApplicationScoped
public class SimpleLivenessCheck implements HealthCheck {

	@Override
	public HealthCheckResponse call() {
		String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");
		String message = "level1componentdb1 live on host " + hostname + "\n";
		
		System.out.println(message);

		return HealthCheckResponse.up(message);
	}
}