<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Query</title>
</head>
<body>
<!-- Main Content -->
<div class="container">
    <div class="row">
        <div class="col-lg-8 col-lg-offset-2 col-md-10 col-md-offset-1">
        
		<c:forEach var="post" items="${postList}">
			<div class="post-preview">
                <a href="/post/${post.id}">
                    <h2 class="post-title">
                        ${post.subject}
                    </h2>
                </a>
                <p class="post-meta">Posted by <a href="#">Origoni</a> on ${post.regDate}</p>
            </div>
            <hr>
		</c:forEach>
		
            <!-- Pager -->
            <ul class="pager">
                <li class="next">
                    <a href="#">Older Posts &rarr;</a>
                </li>
            </ul>
        </div>
    </div>
</div>
</body>
</html>