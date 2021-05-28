<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="jumbotron w-100">
  <div class="container">
    <form action="/hotels/list" method="GET">      
      <div class="row">
        <h1>Where would you like to go?</h1>
        <h5>Type the destiantion of yout travel and we will find the best suiting hotel for you!</h5>
        
          <div class="form-row d-flex justify-content-start w-100">

            <!-- form city -->
            <div class="p-2" style="width: 40%;">
              <input list="cities-list" name="city" type="text" 
                     class="form-control" placeholder="Type city" autocomplete="off"
                     value="${filterInfo.getCity()}">
              <datalist id="cities-list" >
                <c:forEach items="${cities}" var="city">
                  <option><c:out value="${city}"/></option>
                </c:forEach>
              </datalist>
            </div>

            <!-- form submit -->
            <div class="p-2">
              <button type="submit" class="btn btn-dark mb-2">Submit</button>
            </div>

            <!-- forms  sortedby-->
            <div class="p-2">
              <select name="sortedBy" class="custom-select" id="inlineFormCustomSelectPref">
                <option selected>Sort by</option>
                <option value="price_desc">Price descending </option>
                <option value="price_asc">Price ascending</option>
                <option value="grade_desc">Grade descending</option>
                <option value="grade_asc">Grade ascending</option>
                <option value="popularity_desc">Popularity descending</option>
                <option value="popularity_asc">Popularity ascending</option>
              </select>
            </div>

            <div class="p-2">
              <a class="btn btn-light border" data-toggle="collapse" href="#filtersBody" role="button" aria-expanded="false" aria-controls="filtersBody">
                Apply Filters
              </a>
            </div>
          </div>
      </div>

      <div class="row"> 
        <div class="collapse w-100" id="filtersBody">
          <div class="card card-body">
            <div class="row pl-4 pr-4">

              <!-- forms  date (datefrom, dateto)-->
              <div class="col-lg-5 col-md-6">
                <label>Free rooms in period</label>
                <div class="d-flex form-inline">
                  <div class="form-group">
                    <label for="date_from">From</label>
                    <input name="dateFrom" type="date" id="dateFrom" class="form-control mx-sm-3" aria-describedby="passwordHelpInline"
                    value="${filterInfo.getDateFrom()}">
                  </div>

                  <div class="form-group">
                    <label for="date_to">To</label>
                    <input name="dateTo" type="date" id="dateTo" class="form-control mx-sm-3" aria-describedby="passwordHelpInline"
                    value="${filterInfo.getDateTo()}">
                  </div>
                </div>
              </div>

              <div class="col-lg-3 col-md-6 form-group">
                <label for="price">Price</label>

                <!-- forms  price range-->
                <div class="d-flex justify-content-start rounded border w-auto">
                  <div class="form-check m-2">
                    <input class="form-check-input" type="radio" name="priceRange" id="price_medium" value="2"
                      ${filterInfo == null ?  'checked="checked"' : ''}>
                    <label class="form-check-label" for="price_small">
                      Any
                    </label>
                  </div>
                  <div class="form-check m-2">
                    <input class="form-check-input" type="radio" name="priceRange" id="price_medium" value="2"
                      ${filterInfo != null && filterInfo.getPriceRange() == 1 ?  'checked="checked"' : ''}>
                    <label class="form-check-label" for="price_small">
                      $
                    </label>
                  </div>
                  <div class="form-check m-2">
                    <input class="form-check-input" type="radio" name="priceRange" id="price_medium" value="2"
                      ${filterInfo != null && filterInfo.getPriceRange() == 2 ?  'checked="checked"' : ''}>
                    <label class="form-check-label" for="price_medium">
                      $$
                    </label>
                  </div>
                  <div class="form-check m-2">
                    <input class="form-check-input" type="radio" name="priceRange" id="price_large" value="3" 
                    ${filterInfo != null && filterInfo.getPriceRange() == 3 ?  'checked="checked"' : ''}>
                    <label class="form-check-label" for="price_large">
                      $$$
                    </label>
                  </div>
                </div>
              </div>

              <!-- forms  grade (gradeFrom, gradeTo)-->
              <div class="col-lg-3 col-md-6 form-group">
                <label for="grade">Grade</label>
                <div class="d-flex justify-content-around">
                  <select name="gradeFrom" class="custom-select mp-2" id="inlineFormCustomSelectPref">
                    <option value="0" ${filterInfo == null || filterInfo.getGradeFrom() == 0 ?  'selected="selected"' : ''}>
                      From
                    </option>
                    <option value="1" ${filterInfo != null && filterInfo.getGradeFrom() == 1 ?  'selected="selected"' : ''}>
                      1
                    </option>
                    <option value="2" ${filterInfo != null && filterInfo.getGradeFrom() == 2 ?  'selected="selected"' : ''}>
                      2
                    </option>
                    <option value="3" ${filterInfo != null && filterInfo.getGradeFrom() == 3 ?  'selected="selected"' : ''}>
                      3
                    </option>
                    <option value="4" ${filterInfo != null && filterInfo.getGradeFrom() == 4 ?  'selected="selected"' : ''}>
                      4
                    </option>
                    <option value="5" ${filterInfo != null && filterInfo.getGradeTo() == 5 ?  'selected="selected"' : ''}>
                      5
                    </option>
                  </select>
                  
                  <select name="gradeTo" class="custom-select ml-2 mp-2" id="inlineFormCustomSelectPref">
                    <option value="0" ${filterInfo == null || filterInfo.getGradeTo() == 0 ?  'selected="selected"' : ''}>
                      To
                    </option>
                    <option value="1" ${filterInfo != null && filterInfo.getGradeTo() == 1 ?  'selected="selected"' : ''}>
                      1
                    </option>
                    <option value="2" ${filterInfo != null && filterInfo.getGradeTo() == 2 ?  'selected="selected"' : ''}>
                      2
                    </option>
                    <option value="3" ${filterInfo != null && filterInfo.getGradeTo() == 3 ?  'selected="selected"' : ''}>
                      3
                    </option>
                    <option value="4" ${filterInfo != null && filterInfo.getGradeTo() == 4 ?  'selected="selected"' : ''}>
                      4
                    </option>
                    <option value="5" ${filterInfo != null && filterInfo.getGradeTo() == 5 ?  'selected="selected"' : ''}>
                      5
                    </option>
                  </select>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </form>
  </div>
</div>

