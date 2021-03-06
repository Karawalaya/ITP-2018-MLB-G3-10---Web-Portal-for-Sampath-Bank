package CONTROLLER_SERVLET.inventory_management;

import java.io.IOException;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import DAO_SERVICE.inventory_management.DeleteDAO;
import DAO_SERVICE.inventory_management.GeneratePrimaryKey;
import DAO_SERVICE.inventory_management.InsertDAO;
import DAO_SERVICE.inventory_management.RetreiveDAO;
import DAO_SERVICE.inventory_management.UpdateDAO;
import POJO_MODEL.inventory_management.HistoryItem;
import POJO_MODEL.inventory_management.item;
import POJO_MODEL.inventory_management.loginUser;

/**
 * Servlet implementation class deleteItemServlet
 */
public class deleteItemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public deleteItemServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		loginUser u1 = (loginUser) session.getAttribute("IM_user");
		
		int input = JOptionPane.showConfirmDialog(new JDialog(),"Press Okay to confirm", "Customized Dialog", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
	
		if(input==0) {
		
		
		try {
			item i1= RetreiveDAO.retreiveInventoryRow(request.getParameter("id"));
			HistoryItem h1 = new HistoryItem(GeneratePrimaryKey.generateHistoryId(), u1.getUsername(), i1.getItemName(), i1.getItemId(), "", Integer.parseInt("-"+ RetreiveDAO.getItemQty(i1.getItemId())),RetreiveDAO.getItemQty(i1.getItemId()) , 0);
			InsertDAO.insertDeleteHistory(h1);
			if (DeleteDAO.deleteItem(request.getParameter("id"))) {
				
				DeleteDAO.deleteRequest_itemdeleted(i1.getItemId());
				
				JOptionPane pane = new JOptionPane("Successfully Deleted",JOptionPane.OK_OPTION);  
				JDialog dialog = pane.createDialog("Status");  
				dialog.setAlwaysOnTop(true);  
				dialog.show(); 
				
				DeleteDAO.deleteBranchItem(request.getParameter("id"));
				
				ArrayList<item> list = RetreiveDAO.getItems();
				 session = request.getSession();
				session.setAttribute("IM_itemList", list);
				
				request.getRequestDispatcher("IM_ShowItemListEmployee.jsp").forward(request, response);
			}
			else {
				JOptionPane pane = new JOptionPane("Delete Unsuccessful",JOptionPane.OK_OPTION);  
				JDialog dialog = pane.createDialog("Status");  
				dialog.setAlwaysOnTop(true);  
				dialog.show(); 
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		else {
			ArrayList<item> list;
			try {
				list = RetreiveDAO.getItems();
				session = request.getSession();
				session.setAttribute("IM_itemList", list);
				
				request.getRequestDispatcher("IM_ShowItemListEmployee.jsp").forward(request, response);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		
		
		}
		
		
	}

}
