package servico;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import dao.EmprestimoDAO;
import dao.UsuarioDAO;
import modelo.Emprestimo;
import modelo.Material;
import modelo.Usuario;


// abstract
public class EmprestimoControle {
	
	private boolean pode_fazer_emprestimo(Usuario usuario) {
		if(usuario.get_tipo() == "Aluno") {
			UsuarioDAO usuarioDAO = new UsuarioDAO();
			return (usuarioDAO.buscar_matricula(usuario.get_matricula()).size() == 0);
		}
		return true;
	}
	
	
	
	public void cadastrar(String usuario_id, String material_id, int prazo) {
		
		EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
		
		UsuarioControle usuario_controle = UsuarioControle.getInstance();
		List<Usuario> lst_usuario = usuario_controle.buscar_matricula(usuario_id);
		Usuario usuario = lst_usuario.get(0);
		MaterialControle material_controle = MaterialControle.getInstance();
		Material material = material_controle.buscar_id(Integer.parseInt(material_id));

		if(usuario.get_tipo().equals("Aluno") && emprestimoDAO.get_por_id_usuario(usuario.get_id()) > 0) {
			System.out.println("O usuario não pode fazer emprestimo");
			return;
		}
		
		if(emprestimoDAO.get_emprestado_material(Integer.parseInt(material_id)) > 0) {
			System.out.println("Material esta emprestado");
			return;
		}
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		Date date = new Date();
		Emprestimo emprestimo = new Emprestimo(usuario, material, date, prazo, 1);
	
		emprestimoDAO.cadastrar(emprestimo);
		System.out.println("email");
		// enviando um email comprovante
		new Email(usuario, material, "Emprestimo").sendEmail();
	}
	
	
	public void remover(String matricula, String id_material) {
		MaterialControle material_controle = MaterialControle.getInstance();
		Material material = material_controle.buscar_id(Integer.parseInt(id_material));
		Usuario usuario;
		UsuarioControle usuario_controle = UsuarioControle.getInstance();
		List<Usuario> lst_usuario = usuario_controle.buscar_matricula(matricula);
		usuario = lst_usuario.get(0);
		EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
		emprestimoDAO.remover(usuario, material);
	}
	
}
