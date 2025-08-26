/**
 * @name MSA Service HTTP Calls
 * @description Detects HTTP calls between microservices using WebClient
 * @kind problem
 * @problem.severity info
 * @id java/msa-webclient-calls
 * @tags architecture
 *       microservices
 *       http-calls
 */

import java

class WebClientUriCall extends MethodAccess {
  WebClientUriCall() {
    this.getMethod().hasName("uri") and
    this.getQualifier().getType().toString().matches("%WebClient%")
  }
  
  string getUriPattern() {
    exists(Expr arg | 
      arg = this.getArgument(0) and
      result = arg.toString()
    )
  }
}

class ServiceClientClass extends Class {
  ServiceClientClass() {
    this.getName().matches("%ServiceClient") or
    this.getName().matches("%Client")
  }
}

from WebClientUriCall call, ServiceClientClass client, Method method
where 
  call.getEnclosingCallable() = method and
  method.getDeclaringType() = client
select call, "MSA HTTP Call: " + client.getName() + "." + method.getName() + " calls " + call.getUriPattern()