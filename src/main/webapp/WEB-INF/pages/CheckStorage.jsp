<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
  <title>Check storage</title>
</head>
<body>
<div>
  <c:if test="${photoFileService.sourceCorrect}"><p>Source <strong>${photoFileService.sourceStorage}</strong> is correct!</p>
  </c:if>
  <c:if test="${photoFileService.sourceCorrect}"><p>Destination <strong>${photoFileService.destStorage}</strong> is correct!</p>
  </c:if>

  <c:if test="${not empty filesList}">
    <div><h4>Результаты:</h4><p>
      <c:forEach items="${filesList}" var="aFile" varStatus="indx">
        ${aFile}<br/>
      </c:forEach>
    </p></div>
  </c:if>
</div>
<h4>
  Check storage:
</h4>
<form action="<c:url value="/checkStorage.htm"/>" method="post">
  <fieldset>
    <label>
      <span>Source storage path:</span>
      <input type="text" name="sourceDir" value="${photoFileService.sourceStorage}"/>
    </label><br/>
    <label>
      <span>Destination storage path:</span>
      <input type="text" name="destDir" value="${photoFileService.destStorage}"/>
    </label><br/>
    <label>
      <span>Source file:</span>
      <input type="text" name="sourceFile" value=""/>
    </label><br/>
   <input type="submit" value="Submit"/>
  </fieldset>
</form>
</body>
</html>