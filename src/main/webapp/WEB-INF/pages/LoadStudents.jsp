<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
  <title>Load Students</title>
</head>
<body>
<div>
  <c:if test="${not empty messages}">
    <div><h4>Результаты:</h4><p>
      <c:forEach items="${messages}" var="message" varStatus="indx">
        ${message}<br/>
      </c:forEach>
    </p></div>
  </c:if>
</div>
<h4>
  Load Students from file:
</h4>
<form action="<c:url value="/loadStudents.htm"/>" method="post"  enctype="multipart/form-data">
  <fieldset>
    <label>
      <span>Select a file:</span>
      <input type="file" name="studentsFile" />
    </label><br/>
    <label>
      <span>Anglophones:</span>
      <input type="checkbox" name="anglophones" value="true"/>
    </label><br/>
    <label>
      <span>Rewrite photo:</span>
      <input type="checkbox" name="doPhotos" value="true"/>
    </label><br/><br/>
    <label>
      <span>First record:</span>
      <input type="text" name="firstRecord" value="0"/>
    </label><br/>
    <label><br/>
      <span>Total records:</span>
      <input type="text" name="totalRecords" value="0"/>
    </label><br/>
    <input type="submit" value="Submit"/>
  </fieldset>
</form>
</body>
</html>