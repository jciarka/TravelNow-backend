<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="../header.jsp"/>

<div class="jumbotron w-100">
  <div class="container">
      <div class="row">
      <div class="d-flex">
        <div class="p-2">

          <div class="carousel slide" data-ride="carousel">
            <div class="carousel-inner">

              <c:forEach var="imageId" items="${hotel.getPicturesIds()}">        
                <div class="carousel-item active">
                  <img class="d-block w-100 rounded rounded-lg" src="/hotels/hotelDetails/${imageId}" width="400" height="300"  >
                </div>
              </c:forEach>

            </div>
          </div>
        </div>
        <div class="p-4">
          
            <h2>${hotel.getName()}</h2>
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
    </div>

    <!-- forms  date (datefrom, dateto)-->
    <div class="row justify-content-center">
        <h5>Check rooms availability</h5>
    </div>

    <div class="row justify-content-center">
      <form action="/hotels/hotelDetails/${hotel.getId()}" method="GET">    
        <div class="d-flex form-inline">
          <div class="form-group m-2">
            <label for="date_from">From</label>
            <input name="dateFrom" type="date" id="dateFrom" class="form-control mx-sm-3" aria-describedby="passwordHelpInline"
                   value="${dateFrom}">
          </div>

          <div class="form-group m-2">
            <label for="date_to">To</label>
            <input name="dateTo" type="date" id="dateTo" class="form-control mx-sm-3" aria-describedby="passwordHelpInline"
                   value="${dateTo}">
          </div>

          <input type="hidden" name="returnurl" value="${returnurl}">

          <button type="submit" class="btn btn-dark m-2">Submit</button>
        </div>
      </form>
    </div>
  </div>
</div>

<c:if test="${freeRooms.size() > 0}">
  <div class="row mt-4 justify-content-center">
    <h3>Rooms availabile</h>
  </div>
</c:if>

<div class="container">
  <div class="row mt-4 mb-4">
    <c:forEach var="freeRoom" items="${freeRooms}">
      <div class="card rounded rounded-lg w-100 shadow border m-2"  style="border: #8f8f8fb6;" >
        <div class="d-flex justify-content-between align-items-center pl-3 pr-3">
        
          <div class="p-2">
            <c:choose>
              <c:when test="${loggedin}">
                <a href="#" class="btn btn-warning">Book</a>
              </c:when>
              <c:otherwise>
                <a href="/login" class="btn btn-danger">Log in before booking</a>             
              </c:otherwise>
            </c:choose>
          </div>

          <div class="p-2">
            <label>Room number: </label>
          </div>

          <div class="p-2">
            <label>Price: $${freeRoom.getPrice()}</label>
          </div>

          <div class="p-2">
            <label>
                Capacity:
                <c:forEach var = "i" begin = "1" end = "${freeRoom.getCapacity()}">
                  <i class="fa fa-male" aria-hidden="true"></i> 
                </c:forEach>
              </label>
          </div>
        </div>
      </div>
    </c:forEach>
  </div>
</div>

<div class="jumbotron w-100">
  <div class="container">
    <div class="row">
      Tu komentarze
    </div>
  </div>
</div>

<jsp:include page="../footer.jsp"/>