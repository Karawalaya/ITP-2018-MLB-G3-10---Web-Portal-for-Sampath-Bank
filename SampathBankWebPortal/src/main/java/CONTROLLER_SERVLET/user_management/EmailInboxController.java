package CONTROLLER_SERVLET.user_management;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import DAO_SERVICE.user_management.EmailDAO;
import POJO_MODEL.employee_hr_payroll_management.Employee;
import POJO_MODEL.user_management.Email;

/**
 * Servlet implementation class EmailInboxController
 */
public class EmailInboxController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Employee employee =  (Employee) session.getAttribute("employee");
		
		Collection<Email> inboxRetrieve = EmailDAO.inboxRetrieve(employee.getCompanyEmail());
		
		session.setAttribute("inboxRetrieve", inboxRetrieve);
		response.sendRedirect("/SampathBankWebPortal/jsp/user_management/UM_EmailInbox.jsp");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String view = request.getParameter("view");
		String delete = request.getParameter("delete");
		
		if(view != null)
			viewEmail(request, response);
		else if(delete != null)
			deleteEmail(request, response);
	}
	
	private void viewEmail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Collection<Email> inboxRetrieve = (ArrayList<Email>) session.getAttribute("inboxRetrieve");
		
		int emailId = Integer.parseInt(request.getParameter("emailId"));
		
		Email em = null;
		for(Email email: inboxRetrieve) {
			if(email.getEmailId() == emailId) {
				em = email;
				break;
			}
		}
		
		request.setAttribute("email", em);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/user_management/UM_SingleEmail.jsp");
		rd.forward(request, response);
	}

	private void deleteEmail(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		Collection<Email> inboxRetrieve = (ArrayList<Email>) session.getAttribute("inboxRetrieve");
		
		int emailId = Integer.parseInt(request.getParameter("emailId"));
		
		if(EmailDAO.deleteFromInbox(emailId)) {
			PrintWriter out = response.getWriter();
			out.println("<script type=\"text/javascript\">");
			out.println("alert('Email deleted from inbox!');");
			out.println("location='/SampathBankWebPortal/EmailInboxController';");
			out.println("</script>");
		} else {
			PrintWriter out = response.getWriter();
			out.println("<script type=\"text/javascript\">");
			out.println("alert('Email was not deleted from inbox!');");
			out.println("location='/SampathBankWebPortal/EmailInboxController';");
			out.println("</script>");
		}
		
		
	}
}
