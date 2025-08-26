/**
 * @name Find all WebClient usage
 * @description Simply finds any WebClient usage in the codebase
 * @kind problem
 * @problem.severity info
 * @id java/webclient-usage
 * @tags test
 */

import java

from Variable v
where v.getType().toString().matches("%WebClient%")
select v, "Found WebClient variable: " + v.getName() + " in " + v.getCallable().getName()