<%@ page language="java" contentType="text/html; charset=ISO-8859-1" isELIgnored="false" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    <link rel="stylesheet" href=https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css>

    <title>Travel Now!</title>

    <!-- styl który realizuje funkcjonalonść hover dla menu dropdown-->
    <style>

        .dropdown {
        position: relative;
        display: inline-block;
        }

        .dropdown-content {
        display: none;
        position: absolute;
        right: 20px;
        background-color: #f9f9f9;
        min-width: 160px;
        box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
        border-radius: 20px;
        z-index: 1;
        }


        .dropdown:hover .dropdown-content {
        display: block;
        }

        .dropdown-content a {
        border-radius: 20px;
        color: black;
        padding: 12px 16px;
        text-decoration: none;
        display: block;
        }

        .dropdown-content a:hover {background-color: #f1f1f1}

        .dropdown:hover .dropdown-content {
        display: block;
        }


        .nav-button
        {
            position: relative; 
            top: 5px;
        }

        .prompt
        {
            position: relative;
            bottom: 5px;
            color: rgb(0, 51, 51);
        }

        .card-horizontal {
            display: flex;
            flex: 1 1 auto;
        }

        a {
        color: black;
        text-decoration: none;
        }

        a:hover {
        color: black;
        text-decoration: none;
        }

    </style>
</head>

<body>

    <div class="container">
        <div class="row flex-nowrap justify-content-center align-items-center">
          <div class="m-4">
            <a class="text-muted" href="#">
                <div class="d-flex justify-content-center">
                    <img class="mr-4" src="../../icons/airplane.svg" width="60" height="60">
                    <h1> Travel now! </h1>
                </div>
            </a>
          </div>   
        </div>

    </div>

    <!-- NAVBAR -->
    <nav class="navbar navbar-expand-lg navbar-light shadow-lg border" style="border: #8f8f8fb6;" >

        <!-- LINKS -->
        <div class="navbar-brand text-center">
            <div>
                <div class="prompt">
                    Stay wherever you want!
                </div>
                <div class="d-flex justify-content-start">
                    <a href="/">
                        <button class="btn btn-light rounded-1 mr-3 nav-button">
                            <img src="../../icons/hotel-building.svg" width="20" height="20"/>
                            Hotels
                        </button>
                    </a>
                    <a href="#">
                        <button class="btn btn-light rounded-1 mr-3 nav-button">
                            <img src="../../icons/restaurant.svg" width="20" height="20"/>
                            Restaurants
                        </button>
                    </a>
                    <a href="#">
                        <button class="btn btn-light rounded-1 mr-3 nav-button">
                            <img src="../../icons/city-hall.svg" width="20" height="20"/>
                            History
                        </button>
                    </a>
                    <a href="#">
                        <button class="btn btn-light rounded-1 mr-3 nav-button">
                            <img src="../../icons/popcorn.svg" width="20" height="20"/>
                            Atractions
                        </button>
                    </a>
                    <a href="#">
                        <button class="btn btn-light rounded-1 mr-3 nav-button">
                            <img src="../../icons/football.svg" width="20" height="20"/>
                            Sportabsolute
                        </button>
                    </a>
                </div>
            </div>
        </div>

        <!-- LOGIN -->
        <div class="collapse navbar-collapse justify-content-end" id="navbarSupportedContent">
            <ul class="navbar-nav">
                <li class="nav-item active">


                    <c:choose>
                        <c:when test="${loggedin == false}">


                            <div class="nav-item dropdown position-static justify-content-end">
                                <a href="/login">
                                    <button class="btn btn-light rounded-50 dropbtn" style="display: block;"> 
                                        <img src="../../icons/login.png" width="30" height="30" style="fill: white;">
                                        <div>Log in!</div>
                                    </button> 
                                </a>
                            </div>

                        </c:when>

                        <c:otherwise>
                            <div class="nav-item dropdown position-static justify-content-end">
                                <button class="btn btn-light rounded-50 dropbtn" style="display: block;"> 
                                    <img src="../../icons/login.png" width="30" height="30" style="fill: white;">
                                    <div>Account</div> 
                                </button> 
                                <div class="dropdown-content">
                                    <a href="#">
                                        Manage account
                                    </a>
                                    <a href="#">
                                        My reservations
                                    </a>
                                    <a href="/logout">
                                        Log out <img class="bl-2" src="../../icons/exit.svg" width="15" height="15">
                                    </a>
                                </div>
                            </div>
                        </c:otherwise>  

                    </c:choose>


                </li>
            </ul>
        </div>
    </nav>


    