<%-- 
    Document   : payment
    Created on : 12 Jan, 2023, 7:42:55 PM
    Author     : prath
--%>

<%@ page import="java.net.URLDecoder" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%
    // Retrieve the parameters from the URL
    String bookingId = request.getParameter("bookingId");
    String hostelName = request.getParameter("hostelName");
    String price = request.getParameter("price");

    // Optionally, you can decode the parameters if needed
    if (bookingId != null) {
        bookingId = URLDecoder.decode(bookingId, "UTF-8");
    }
    if (hostelName != null) {
        hostelName = URLDecoder.decode(hostelName, "UTF-8");
    }
    if (price != null) {
        price = URLDecoder.decode(price, "UTF-8");
    }

    // You can now use these variables in your JSP code
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Payment</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
        <link rel="stylesheet" href="css\bootstrap.css" type="text/css">
        <link rel="stylesheet" href="css\user\payment.css" type="text/css">
        
        <script>
            
        function scanpay() 
        {
                var y = document.getElementById("cardpayid");
                y.style.display="none";
                var x = document.getElementById("scanpayid");
                if (x.style.display === "none") {
                        x.style.display = "block";
                } else {
                        x.style.display = "none";
                }
        }
        
        function cardpay()
        {
                var y = document.getElementById("scanpayid");
                y.style.display="none";
                var x = document.getElementById("cardpayid");
                if (x.style.display === "none") {
                        x.style.display = "block";
                } else {
                        x.style.display = "none";
                }
        }
        </script>
    </head>
    <body>
        
        <br>
        
        <form name="payment" action="PaySuccess.jsp" method="post">
        <!-- Include hidden input fields for bookingId and price -->
        <input type="hidden" name="bookingId" value="<%=bookingId%>">
        <input type="hidden" name="price" value="<%=price%>">
        <div class="container" style="margin-left: 5%;margin-top: 9%;">
                 
                <!-- Credit Card Payment Form - START -->
                <div class="container">
                    <div class="row">
                        <div class="col-xs-12 col-md-6 col-md-offset-4">
                            
                            <div class="row" style="height: 25px; background-color: white; width: 100%; margin-left: 0%;">
                                
                                        <div class="col-sm-1"></div>
                                        <div class="col-sm-5">
                                            <input type="radio" name="pay" value="Scan Payment" onclick="scanpay()">
                                            &nbsp;
                                            <label style="font-size: 18px;">Scan & Pay</label> 
                                        </div>
                                        <div class="col-sm-1"></div>
                                        <div class="col-sm-6">
                                            <input type="radio" name="pay" value="Card Payment" onclick="cardpay()" checked="true">
                                            &nbsp;
                                            <label style="font-size: 18px;">Card Payment</label> 
                                        </div>
                            </div>
                            
                            <br>
                            <div class="panel panel-default" id="cardpayid"  >
                                <div class="panel-heading">
                                    <div class="row">
                                        <h3 class="text-center">Payment Details</h3>
                                        <div class="inlineimage"> 
                                            <img class="img-responsive images" src="https://cdn0.iconfinder.com/data/icons/credit-card-debit-card-payment-PNG/128/Mastercard-Curved.png" style="margin-left: 20%;margin-top: -2%;height: 15%; width: 15%;"> 
                                           
                                            <img class="img-responsive images" src="https://cdn0.iconfinder.com/data/icons/credit-card-debit-card-payment-PNG/128/Paypal-Curved.png" style="margin-left:65%; margin-top:-15%; height: 15%; width: 15%;"> 
                                    
                                        </div>
                                    </div>
                                </div>
                                <div class="panel-body">
                                    
                                    

                                        <div class="row">
                                            <div class="col-xs-12">
                                                <div class="form-group"> <label>CARD NUMBER</label>
                                                    <div class="input-group"> <input type="text"  name="card_no" class="form-control" placeholder="Valid Card Number" /> <span class="input-group-addon"><span class="fa fa-credit-card"></span></span> </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-xs-7 col-md-7">
                                                <div class="form-group"> <label><span class="hidden-xs">EXPIRATION</span><span class="visible-xs-inline">EXP</span> DATE</label> <input type="month" name="expiretion_date" class="form-control"  /> </div>
                                            </div>
                                            <div class="col-xs-5 col-md-5 pull-right">
                                                <div class="form-group"> <label>CVV CODE</label> <input type="text" name="cv_code"  class="form-control" placeholder="CVV" /> </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-xs-12">
                                                <div class="form-group"> <label>CARD OWNER</label> <input type="text" name="card_owner" class="form-control" placeholder="Card Owner Name" /> </div>
                                            </div>
                                        </div>

                                </div>
                                <div class="panel-footer">
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <button type="submit" name="submit" value="Pay Now" class="btn btn-success btn-lg btn-block">
                                                Pay Now ( ₹ : <%=price%> )
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                                    
                                    
                       <div class="panel panel-default" id="scanpayid" style="display: none">
                           <img src="images/qr.png" style="height: 400px; width: 400px; margin-left: 80px;">     
                           
                           
                                <div class="panel-footer">
                                    <div class="row">
                                        <div class="col-xs-12"> 
                                            <button type="submit" name="submit" value="Pay Now" class="btn btn-success btn-lg btn-block">
                                                Pay Now ( ₹ : <%=price%> )
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                                    
                        </div>
                    </div>
                </div>
            </div>
        </form>
        
    </body>
</html>
