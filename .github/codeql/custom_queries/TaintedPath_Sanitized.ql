/**
 * @name Uncontrolled and unsanitized data used in path expression
 * @description Accessing paths influenced by users can allow an attacker to access unexpected resources.
 *              Checks for path sanitation as identified through Java annotation.
 * @kind path-problem
 * @problem.severity error
 * @security-severity 7.5
 * @precision high
 * @id java/path-injection-unsanitized
 * @tags security
 *       external/cwe/cwe-022
 *       external/cwe/cwe-023
 *       external/cwe/cwe-036
 *       external/cwe/cwe-073
 */


import java
import semmle.code.java.dataflow.FlowSources
import semmle.code.java.security.PathCreation
import DataFlow::PathGraph
import TaintedPathCommon

class HasPathSanitizer extends DataFlow::BarrierGuard {
  HasPathSanitizer() {
    this.(MethodAccess).getMethod().getAnAnnotation() instanceof PathSanitizerAnnotation
  }

  override predicate checks(Expr e, boolean branch) {
    e = this.(MethodAccess).getQualifier() and branch = false
  }
}

class PathSanitizerAnnotation extends Annotation {
    PathSanitizerAnnotation() {
        this.getType().hasQualifiedName("drivenflywheel.examples.annotations.security.guards", "PathSanitizer")
    }
}

class PathSanitizedMethod extends Method {
    PathSanitizedMethod() {
        this.getAnAnnotation() instanceof PathSanitizerAnnotation
    }
}

class TaintedPathConfig extends TaintTracking::Configuration {

  TaintedPathConfig() { this = "TaintedPathConfig" }

  override predicate isSource(DataFlow::Node source) { source instanceof RemoteFlowSource }

  override predicate isSink(DataFlow::Node sink) {
    exists(Expr e | e = sink.asExpr() | e = any(PathCreation p).getAnInput() and not guarded(e))
  }

  override predicate isSanitizer(DataFlow::Node node) {
    exists(Type t | t = node.getType() | t instanceof BoxedType or t instanceof PrimitiveType)
  }

  override predicate isSanitizerGuard(DataFlow::BarrierGuard guard) {
    guard instanceof HasPathSanitizer
  }
}


from DataFlow::PathNode source, DataFlow::PathNode sink, PathCreation p, TaintedPathConfig conf
where
  sink.getNode().asExpr() = p.getAnInput() and
  conf.hasFlowPath(source, sink)
select p, source, sink, "$@ flows to here and is used in a path.", source.getNode(), "User-provided value"
