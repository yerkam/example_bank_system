package banking.presentation.menu;

public class RoleContext {
	private RoleStrategy roleStrategy;
	
	public RoleContext(RoleStrategy roleStrategy) {
		this.roleStrategy = roleStrategy;
	}
	
	public void showMenu() {
		roleStrategy.showMenu();
	}
}
