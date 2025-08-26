/**
 * @name HTTP service calls in MSA
 * @description Finds HTTP calls between microservices
 * @kind problem
 * @problem.severity info
 * @id java/msa-service-calls
 * @tags maintainability
 *       architecture
 *       microservices
 */

import java

from MethodAccess call, Method method
where
  // WebClient calls
  call.getMethod().hasName("uri") and
  call.getQualifier().getType().hasQualifiedName("org.springframework.web.reactive.function.client", "WebClient$RequestHeadersUriSpec")
  or
  // RestTemplate calls
  call.getMethod().hasName("getForObject") or
  call.getMethod().hasName("postForObject") or
  call.getMethod().hasName("exchange")
  or
  // HTTP URL construction
  exists(StringLiteral url |
    url.getValue().matches("%http://%") and
    url.getEnclosingCallable() = method and
    call.getEnclosingCallable() = method
  )
select call, "HTTP service call detected: " + call.toString() + " in method " + method.getName()