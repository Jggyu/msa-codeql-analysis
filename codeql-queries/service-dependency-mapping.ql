/**
 * @name Service dependency mapping
 * @description Maps dependencies between microservices based on HTTP calls
 * @kind problem
 * @problem.severity info
 * @id java/msa-dependency-mapping
 * @tags architecture
 *       microservices
 *       dependency-analysis
 */

import java

class ServiceClient extends Class {
  ServiceClient() {
    this.getName().matches("%ServiceClient") or
    this.getName().matches("%Client")
  }
}

class WebClientCall extends MethodAccess {
  WebClientCall() {
    this.getMethod().hasName("uri") and
    this.getQualifier().getType().getName().matches("WebClient%")
  }
  
  string getTargetUrl() {
    exists(StringLiteral url |
      url = this.getArgument(0) and
      result = url.getValue()
    )
  }
}

from ServiceClient client, WebClientCall call, Method method, StringLiteral url
where
  call.getEnclosingCallable().getDeclaringType() = client and
  method = call.getEnclosingCallable() and
  url.getValue().matches("%http://%") and
  url.getEnclosingCallable() = method
select client, 
  "Service " + client.getPackage().getName().suffix(client.getPackage().getName().indexOf("example.") + 8) + 
  " depends on external service at: " + url.getValue() + 
  " (called from method: " + method.getName() + ")"