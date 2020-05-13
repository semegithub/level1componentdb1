package edu.cloudnative.poc;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class DatabaseConnectionHealthCheck implements HealthCheck {
//	int i = 0;

	@Override
	public HealthCheckResponse call() {
		String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");
		String message = "level1componentdb1 ready on host " + hostname + "\n";

		HealthCheckResponse response = null;
//		if (i < 10) {
//			i++;
			response = HealthCheckResponse.up(message);
			System.out.println(message);
//		} else
//			response = HealthCheckResponse.down(message);

		return response;
	}
}