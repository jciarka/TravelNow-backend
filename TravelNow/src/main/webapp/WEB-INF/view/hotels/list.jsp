<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="../header.jsp"/>

<jsp:include page="whereto.jsp"/>

<c:forEach var="hotel" items="${hotels}">

  <div class="container">
    <div class="row">
      <div class="card rounded rounded-lg w-100 shadow border"  style="border: #8f8f8fb6;" >
        <a href="/hotels/hotelDetails/${hotel.getId()}?returnurl=<%= request.getRequestURI() %>&dateFrom=${filterInfo.getDateFrom()}&dateTo=${filterInfo.getDateTo()}" style="width: 100;">
          <div class="card-horizontal">
            <div class="img-square-wrapper rounded rounded-lg">
                <img class="rounded rounded-lg" src="" alt="" width="300" height="200"  >
            </div>
            <div class="card-body">
              <h4 class="card-title"><c:out value="${hotel.getName()}"/></h4>
              <p class="card-text"><c:out value="${hotel.getDescription()}"/></p>
              <c:out value="${hotel.getNumOfRooms()}"/> rooms
              </br>
              <c:forEach var = "i" begin = "1" end = "${hotel.getAvgRating()}">
                <i class="fa fa-star" aria-hidden="true"></i>
              </c:forEach>
              </br>
              <c:choose>
                <c:when test="${hotel.getAvgPrice() < 50}">
                  $
                </c:when>    
                <c:when test="${hotel.getAvgPrice() < 120}">
                  $$
                </c:when>
                <c:otherwise>
                  $$$
                </c:otherwise>
              </c:choose>
            </div>
          </div>
        </a>
      </div>
    </div>
  </div>
</c:forEach>

<jsp:include page="../footer.jsp"/>
