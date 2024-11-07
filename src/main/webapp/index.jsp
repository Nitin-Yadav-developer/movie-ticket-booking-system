<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Movie ticket Boking System</title>


<script>
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            var lat = position.coords.latitude;
            var lon = position.coords.longitude;
            document.getElementById("location").value = `Lat: ${lat}, Lon: ${lon}`;
        });
    } else {
        document.getElementById("location").placeholder = "Geolocation not supported.";
    }
</script>

<style>
    * {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
    }
    
    body {
        font-family: Arial, sans-serif;
        background-color: rgb(252, 244, 235);
    }
    
    .list-container {
        display: flex;
        justify-content: center; /* Centers the boxes horizontally */
        gap: 10px; /* Adds space between the boxes */
    }
    
    
    .list-item {
        
        border: 2px solid #000;
        background-color: #f2eded;
        height: fit-content;
        width: fit-content;
        text-align: center;

        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Adds some shadow for a box effect */
        transition: transform 0.3s ease; /* For smooth hover effect */
    }
    
    .list-item:hover {
        transform: scale(1.1); /* Enlarges the item slightly on hover */
    }
    h2{
        padding-bottom: 10px;
        padding-top: 10px;
        
    }
    h1{
        padding-bottom: 50px;
    }
     form{
        padding-bottom: 30px;
     }

     .locinput{
       
        
     }
     .searchinput{
        height: 30px;
       
        text-align:center;
        color:black ;
        
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        border:2px solid black;
        border-radius:10px;
        transition: transform 0.3s ease;  
     }
     .searchinput:hover{
        transform: scale(1.1);
     }

     .searchbutton{
        height: 30px;
        
        text-align:center;
        color:black ;
        background-color: rgb(240, 85, 85);
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        border:2px solid black;
        border-radius:15px;
        transition: transform 0.3s ease;
     }
     .searchbutton:hover{
        transform: scale(1.1);
        background-color: rgb(73, 231, 73);
     }
     .space{
        height:10px;
        width: 100%;
        background-color: blanchedalmond;
     }
     .links{
        text-decoration: none;
        color: black;
        

     }
     .links:hover{
        background-color: greenyellow;
        border:4px solid greenyellow;
        border-radius: 4px;
     }

    #menubar{
        display: flex; 
        width: 100%;
        height:50px;
        justify-content: center;

        
    }
    #heading{
        flex: 0 0 60%; 
        padding: 10px; 
        background-color:whitesmoke;
       
        
    }
    #searchinput{
        flex: 0 0 20%; 
        padding: 10px; 
        background-color: whitesmoke; 
         

        

    }
    #searchloc{
        flex: 0 0 20%; 
        padding: 10px; 
        background-color:whitesmoke;
         
    }
    #menu{
        display: flex;
        height:50px;
        width:100%;
        
    }
    #menubox1{
        display:flex;
        height:50px;
        flex: 0 0 50%;
        background-color: blanchedalmond;
       
    }
    #menubox2{
        display:flex;
        flex: 0 0 50%;
        height:50px;
        background-color: blanchedalmond;
        

    }
    #home{
       margin-left: 50%;
       background-color: aquamarine;
      height: fit-content;
      border:4px solid aquamarine;
      border-radius:4px;
      
    }

    #allmovie{
      margin-left: 5%;
      background-color: aquamarine;
      height: fit-content;
      border:4px solid aquamarine;
      border-radius:4px;
    }

    #signin{
        margin-left: 70%;
        background-color: aquamarine;
      height: fit-content;
      border:4px solid aquamarine;
      border-radius:4px;
    }
    #signup{
        margin-left: 10%;
        background-color: aquamarine;
      height: fit-content;
      border:4px solid aquamarine;
      border-radius:4px;
       
    }
    .menul{
         padding: 20px;
         transition: transform 0.3s ease;
     }

     .menul:hover{
        transform: scale(1.1);
        color:blue;
       
       
     }

    
    #menulist{
        border:4px solid black;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        height:400px;
        width:200px;
    }
</style>

</head>

<body>
    <!---header-->
  <div id="menubar">
    <div id="heading">
         <h1 align="center">Movie Ticket Booking System</h1>
    </div>
    <div id="searchinput" >
        <input type="text" class="searchinput" placeholder="search">   
        <button class="searchbutton">Movies</button>
    </div> 
    <div id="searchloc" >
        <input id="location" type="text" class="searchinput" placeholder="search ">
        <button class="searchbutton">Location </button>
    </div>
       
  </div>


    <div class="space">


    </div>

    <div id="menu">
        <div id="menubox1">
        <div id="home"> <a  href=""  class="links">  Home</a></div>
        <div id="allmovie"><a href=" " class="links">All movies</a></div>
         </div>

         <div id="menubox2">
        <div id="signin"><a href="" class="links">Sign in</a></div>
        <div id="signup"><a href="" class="links">Sign up</a></div>
         </div>
    </div>





<!--recent release moviees -->
  <h2 align="center">Recent Release Movies</h2>
<div>


   

    <div class="list-container">
        <div  id="menulist">
            <div class="menul">Dashboard</div>
            <div class="menul">All movies</div>
            <div class="menul">Theaters</div>
            <div class="menul">show</div>
            <div class="menul">Feedback</div>
            <div class="menul">Profile</div>
        </div>
        <div class="list-item"><img src="E:/WebDevelopment/2.jpg" height="200" width="200"><h5>Goat</h5></div>
       

        <div class="list-item"> <img src="E:/WebDevelopment/1.jpg" height="200" width="200"><h5>Stree 2</h5></div>
        <div class="list-item"><img src="E:/WebDevelopment/3.jpg" height="200" width="200"><h5>The Goat Life</h5></div>
        <div class="list-item"><img src="E:/WebDevelopment/4.jpg" height="200" width="200"><h5>Raayan</h5></div>
        <div class="list-item" style="border-radius: 30px;">See more..</div>
    </div>

    



 </div>
</body>
</html>