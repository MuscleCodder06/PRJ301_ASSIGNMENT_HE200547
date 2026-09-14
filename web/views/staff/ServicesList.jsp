<%-- 
    Document   : ServicesList
    Created on : Mar 10, 2026, 3:07:05 AM
    Author     : ADMIN
--%>


<%@page import="models.Service" %>
<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Services List</title>
        <meta charset="UTF-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
    </head>
    <body>
        <div>
            <jsp:include page="../header.jsp"/>
            <!-- Sidebar -->
            <jsp:include page="../topnav.jsp"/>
            
            <% String searchText = request.getParameter("searchText");%>
            <%if (searchText == null) searchText = "";%>
            
            <div>
                
                    <a href="ServicesCreate"><button class="btnCreate" type="button">Add new Services</button></a>
                    
             

            </div>
            <h3>|List of Services</h3>
            <div class="table-container">
                <table class="data-table">
                    <tr>
                        <th>ID</th>
                        <th>ServiceName</th>
                        <th>Price(VND)</th>
                        
                    </tr>
                    <%List<Service> services = (ArrayList<Service>)request.getAttribute("services");%>
                    <%for(Service s : services){%>
                    <%pageContext.setAttribute("s", s);%>
                    <tr>
                        <td>${pageScope.s.getServiceID()}</td>
                        <td>${pageScope.s.getServiceName()}</td>
                        <td>${pageScope.s.getPrice()}</td>
                        

                    </tr>
                    <%}%>
                </table>
            </div>


        </div>
                <div class="errSearch">
            ${requestScope.errorSearch} 
        </div>
        <!-- Footer -->
        <jsp:include page="../footer.jsp"/>

    </div>


<script>
    function handleAction(select) {

        let value = select.value;

        if (value === "")
            return;

        let parts = value.split("-");
        let action = parts[0];
        let id = parts[1];

        if (action === "edit") {
            window.location = "ServicesEdit?id=" + id;
        }

        if (action === "delete") {
            if (confirm("Are you sure to delete?")) {
                window.location = "ServicesDelete?id=" + id;
            }
        }
    }
</script>
</body>
</html>
