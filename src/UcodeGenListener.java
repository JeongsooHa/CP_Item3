
import java.util.HashMap;

import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.stringtemplate.v4.compiler.STParser.ifstat_return;

public class UcodeGenListener extends MiniCBaseListener{
	
	ParseTreeProperty<String> newTexts = new ParseTreeProperty<String>();
	HashMap<String, String> map = new HashMap<>();
	int blockLevel = 1;
	int bgnOffset=1;
	int globalOffset=0;
	int valOffset = 1;

	// program	: decl+
	@Override
	public void exitProgram(MiniCParser.ProgramContext ctx) {
		super.exitProgram(ctx);
		String decl = "";
		String temp = "";
		if(ctx.getChildCount() > 0)						// decl이 하나 이상 존재할 때
			for(int i=0; i<ctx.getChildCount(); i++){
				if(ctx.decl(i).getChild(i) instanceof MiniCParser.Var_declContext)
					temp += newTexts.get(ctx.decl(i));
				else
					decl += newTexts.get(ctx.decl(i));
			}
		newTexts.put(ctx, decl);
		System.out.print(newTexts.get(ctx));
		System.out.println("           bgn "+(bgnOffset-1)+" ");
		System.out.println("           proc "+(bgnOffset-1)+" "+blockLevel+" 1 ");
		if(!temp.equals(""))
			System.out.print(temp);
		System.out.println("           ldp ");
		System.out.println("           call main ");
		System.out.println("           end ");
	}
	
	// decl	: var_decl | fun_decl
	@Override
	public void exitDecl(MiniCParser.DeclContext ctx) {
		super.exitDecl(ctx);
		String decl = "";
		if(ctx.getChildCount() == 1)
		{
			if(ctx.var_decl() != null){
				// decl이 var_decl인 경우
				decl += newTexts.get(ctx.var_decl());
			}
			else									// decl이 fun_decl인 경우
				decl += newTexts.get(ctx.fun_decl());
		}
		newTexts.put(ctx, decl);
	}
	@Override
	public void enterVar_decl(MiniCParser.Var_declContext ctx) {
		super.enterVar_decl(ctx);
		if(ctx.children.size()==3 ||ctx.children.size()==5){
			map.put(ctx.getChild(1).getText(), blockLevel+":"+ Integer.toString(valOffset)+":1");
			valOffset++;
			bgnOffset= valOffset;
		}else if(ctx.children.size()==6){
			int arrSize = Integer.parseInt(ctx.getChild(3).getText());
			map.put(ctx.getChild(1).getText(), blockLevel+":"+ Integer.toString(valOffset)+":"+arrSize+":list");
			valOffset+=arrSize;
			bgnOffset=valOffset;
		}
	}
	
	// var_decl	: type_spec IDENT ';' | type_spec IDENT '=' LITERAL ';'|type_spec IDENT '[' LITERAL ']' ';'
	@Override
	public void exitVar_decl(MiniCParser.Var_declContext ctx) {
		super.exitVar_decl(ctx);
		String var = null;
		String local="";
		if (ctx.getChildCount() == 3) {
			var = ctx.IDENT().getText();
			String rs = map.get(var);
			if(rs!=null){
				String abc[] = rs.split(":");	
				newTexts.put(ctx, "           sym "+abc[0]+" "+abc[1]+" "+abc[2]+" \n");
			}
		}else if(ctx.getChildCount() == 5){
			var = ctx.IDENT().getText();
			String rs = map.get(var);
			if(rs!=null){
				String data1[] = rs.split(":");
				local += "           sym "+data1[0]+" "+data1[1]+" "+data1[2]+" \n";
				local += "           ldc " + ctx.getChild(3).getText() + " \n";
				local += analysisResult("str", rs);
				newTexts.put(ctx, local);
			}			
		}
		else if(ctx.getChildCount() == 6){ 
			var = ctx.IDENT().getText();
			String rs = map.get(var);
			if(rs!=null){
				String abc[] = rs.split(":");	
				newTexts.put(ctx, "           sym "+abc[0]+" "+abc[1]+" "+abc[2]+" \n");
			}
		}
	}
	
	// type_spec	: VOID | INT
	@Override
	public void exitType_spec(MiniCParser.Type_specContext ctx) {
		super.exitType_spec(ctx);
		newTexts.put(ctx, ctx.getChild(0).getText());
	}
	
	// fun_decl : type_spec IDENT '(' params ')' compound_stmt
	@Override
	public void enterFun_decl(MiniCParser.Fun_declContext ctx) {
		super.enterFun_decl(ctx);
		blockLevel++;
		valOffset = 1;
	}

	@Override
	public void exitFun_decl(MiniCParser.Fun_declContext ctx) {
		super.exitFun_decl(ctx);
		String stmt="";
		if(ctx.getChildCount() == 6)
		{
			stmt += calFunc(ctx.IDENT().getText());
			stmt += "proc "+(valOffset-1)+" "+blockLevel+" 2 \n";
			stmt += newTexts.get(ctx.params());
			stmt += newTexts.get(ctx.compound_stmt());
			if(ctx.type_spec().getText().equals("void")){
				stmt += "           ret \n           end \n";
			}
			newTexts.put(ctx, stmt);
		}
		blockLevel--;
	}
	


	// params	: param (',' param)* | VOID
	@Override
	public void exitParams(MiniCParser.ParamsContext ctx) {
		super.exitParams(ctx);
		String params = "";
		if(ctx.getChildCount() > 0)
		{
			if(ctx.getChild(0) == ctx.VOID())					// VOID
				params = ctx.getChild(0).getText();
			else												// param (',' param)*
			{
				for(int i=0; i<ctx.getChildCount(); i++)
				{
					if(i % 2 == 0)
						params += newTexts.get(ctx.param(i/2));	// param
				}
			}
		
		}
		newTexts.put(ctx, params);
	}
	
	// param	: type_spec IDENT | type_spec IDENT '[' ']'
	@Override
	public void exitParam(MiniCParser.ParamContext ctx) {
		super.exitParam(ctx);
		if(ctx.getChildCount() == 2 || ctx.getChildCount()==4)
		{		
			map.put(ctx.IDENT().getText(), blockLevel+":"+ Integer.toString(valOffset)+":1");
				valOffset++;
			String temp = map.get(ctx.IDENT().getText());
			if(temp!=null){
				String abc[] = temp.split(":");	
				newTexts.put(ctx, "           sym "+abc[0]+" "+abc[1]+" "+abc[2]+" \n");
			}

		}
	}
	
	// stmt	: expr_stmt | compound_stmt | if_stmt | while_stmt | return_stmt
	@Override
	public void exitStmt(MiniCParser.StmtContext ctx) {
		super.exitStmt(ctx);
		String stmt = "";
		if(ctx.getChildCount() > 0)
		{
			if(ctx.expr_stmt() != null)				// expr_stmt일 때
				stmt += newTexts.get(ctx.expr_stmt());
			else if(ctx.compound_stmt() != null)	// compound_stmt일 때
				stmt += newTexts.get(ctx.compound_stmt());
			else if(ctx.if_stmt() != null)			// if_stmt일 때
				stmt += newTexts.get(ctx.if_stmt());
			else if(ctx.while_stmt() != null)		// while_stmt일 때
				stmt += newTexts.get(ctx.while_stmt());
			else									// return_stmt일 때
				stmt += newTexts.get(ctx.return_stmt());
		}
		newTexts.put(ctx, stmt);
	}
	
	// expr_stmt	: expr ';'
	@Override
	public void exitExpr_stmt(MiniCParser.Expr_stmtContext ctx) {
		super.exitExpr_stmt(ctx);
		String stmt = "";
		if(ctx.getChildCount() == 2)
		{
			stmt += newTexts.get(ctx.expr());	// expr
		}
		newTexts.put(ctx, stmt);
	}
	
	// while_stmt	: WHILE '(' expr ')' stmt
	@Override
	public void enterWhile_stmt(MiniCParser.While_stmtContext ctx) {
		super.enterWhile_stmt(ctx);
	}

	@Override
	public void exitWhile_stmt(MiniCParser.While_stmtContext ctx) {
		super.exitWhile_stmt(ctx);
		String stmt = "";
		if(ctx.getChildCount() == 5)
		{
			stmt += calFunc("$$"+globalOffset)+"nop \n";
			globalOffset++;
			stmt += newTexts.get(ctx.expr());
			stmt += "           fjp $$"+(globalOffset)+" \n";
			stmt += newTexts.get(ctx.stmt());
			stmt += "           ujp $$"+(globalOffset-1)+" \n";
			stmt += calFunc("$$"+globalOffset)+"nop \n";
			globalOffset++;
		}
		newTexts.put(ctx, stmt);
	}
	
	// compound_stmt	: '{' local_decl* stmt* '}'
	@Override
	public void exitCompound_stmt(MiniCParser.Compound_stmtContext ctx) {
		super.exitCompound_stmt(ctx);
		String stmt = "";
		int local_i = 0, stmt_i = 0;
		if(ctx.getChildCount() >= 2)
		{
			for(int i=1; i<ctx.getChildCount()-1; i++)
			{
				if(ctx.local_decl().contains(ctx.getChild(i)))		// local_decl인 경우
					stmt += newTexts.get(ctx.local_decl(local_i++));
				else												// stmt인 경우
					stmt += newTexts.get(ctx.stmt(stmt_i++));
			}
		}
		newTexts.put(ctx, stmt);
	}
	@Override
	public void enterLocal_decl(MiniCParser.Local_declContext ctx) {
		super.enterLocal_decl(ctx);
		if(ctx.children.size()==3 ||ctx.children.size()==5){
			map.put(ctx.getChild(1).getText(), blockLevel+":"+ Integer.toString(valOffset)+":1");
			valOffset++;
		}else if(ctx.children.size()==6){
			int arrSize = Integer.parseInt(ctx.getChild(3).getText());
			map.put(ctx.getChild(1).getText(), blockLevel+":"+ Integer.toString(valOffset)+":"+arrSize+":"+"list");
			valOffset+=arrSize;
		}
	}
	// local_decl	: type_spec IDENT ';' | type_spec IDENT '[' ']' ';'
	@Override
	public void exitLocal_decl(MiniCParser.Local_declContext ctx) {
		super.exitLocal_decl(ctx);
		String var = null;
		String local="";
		if (ctx.getChildCount() == 3) {
			var = ctx.IDENT().getText();
			String rs = map.get(var);
			if(rs!=null){
				String abc[] = rs.split(":");	
				newTexts.put(ctx, "           sym "+abc[0]+" "+abc[1]+" "+abc[2]+" \n");
			}
		}else if(ctx.getChildCount() == 5){
			var = ctx.IDENT().getText();
			String rs = map.get(var);
			if(rs!=null){
				String data1[] = rs.split(":");
				local += "           sym "+data1[0]+" "+data1[1]+" "+data1[2]+" \n";
				local += "           ldc " + ctx.getChild(3).getText() + " \n";
				local += analysisResult("str", rs);
				newTexts.put(ctx, local);
			}			
		}
		else if(ctx.getChildCount() == 6){ 
			var = ctx.IDENT().getText();
			String rs = map.get(var);
			if(rs!=null){
				String abc[] = rs.split(":");	
				newTexts.put(ctx, "           sym "+abc[0]+" "+abc[1]+" "+abc[2]+" \n");
			}
		}
	}
	
	// if_stmt	: IF '(' expr ')' stmt | IF '(' expr ')' stmt ELSE stmt;
	@Override
	public void enterIf_stmt(MiniCParser.If_stmtContext ctx) {
		super.enterIf_stmt(ctx);
	}

	@Override
	public void exitIf_stmt(MiniCParser.If_stmtContext ctx) {
		super.exitIf_stmt(ctx);
		String stmt = "";
		if(ctx.getChildCount() == 5)
		{
			globalOffset++;
			stmt += newTexts.get(ctx.expr());
			stmt += "           fjp $$"+(globalOffset)+" \n";
			stmt += newTexts.get(ctx.stmt(0));
			stmt += calFunc("$$"+globalOffset)+"nop \n";
			globalOffset++;
			
		}else if(ctx.getChildCount()==7){
			globalOffset++;
			stmt += newTexts.get(ctx.expr());
			stmt += "           fjp $$"+(globalOffset)+" \n";
			stmt += newTexts.get(ctx.stmt(0));
			stmt += "           ujp $$"+(globalOffset+1)+" \n";
			stmt += calFunc("$$"+globalOffset)+"nop \n";
			stmt += newTexts.get(ctx.stmt(1));
			stmt += calFunc("$$"+(globalOffset+1))+"nop \n";
			globalOffset++;
		}
		newTexts.put(ctx, stmt);
	}
	
	// return_stmt	: RETURN ';' | RETURN expr ';'
	@Override
	public void exitReturn_stmt(MiniCParser.Return_stmtContext ctx) {
		super.exitReturn_stmt(ctx);
		String stmt = null;
		stmt = newTexts.get(ctx.expr());

		if (ctx.getChildCount() == 3) {
			newTexts.put(ctx, stmt + "           retv \n");
		}

	}
	
	// expr
	@Override
	public void exitExpr(MiniCParser.ExprContext ctx) {
		super.exitExpr(ctx);
		String expr = "";
		if(ctx.getChildCount() > 0)
		{
			// IDENT | LITERAL일 경우
			if(ctx.getChildCount() == 1){
				String rs = map.get(ctx.getText());
				if(rs==null)					
					expr+= "           ldc " + ctx.getText() + " \n";
				else if(rs.contains("list")){
					String var[]=rs.split(":");
					expr+= "           lda " + var[0]+" "+var[1] + " \n";
				}else{
					String var[]=rs.split(":");
					expr+= "           lod " + var[0]+" "+var[1] + " \n";
				}
			}
			// '!' expr | '-' expr | '+' expr일 경우
			else if(ctx.getChildCount() == 2)
			{
				String op = ctx.getChild(0).getText();
				String rs = map.get(ctx.expr(0).getText());
				switch (op) {
				case "!":
					expr += newTexts.get(ctx.expr(0))+"           notop \n";					
					break;
				case "-":
					expr += newTexts.get(ctx.expr(0))+"           neg \n";
					break;
				case "+":
					
					break;
				case "--":
					expr += newTexts.get(ctx.expr(0))+"           dec \n";
					break;
				case "++":
					expr += newTexts.get(ctx.expr(0))+"           inc \n";
					break;
				default:
					break;
				}
				if(rs!=null)
					expr += analysisResult("str", rs);
			}
			else if(ctx.getChildCount() == 3)
			{
				// '(' expr ')'
				if(ctx.getChild(0).getText().equals("("))
				{
					expr += newTexts.get(ctx.expr(0));
				}
				// IDENT '=' expr
				else if(ctx.getChild(1).getText().equals("="))
				{
					String rs = map.get(newTexts.get(ctx.expr(0)));
					if(rs==null)
						expr +=newTexts.get(ctx.expr(0));
					else
						expr +=analysisResult("lod",rs);
					
					rs = map.get(ctx.getChild(0).getText());
					if(rs!=null){
						String var[] = rs.split(":");
						expr += "           str "+var[0]+" "+var[1]+" \n";
					}
				}
				// binary operation
				else
				{
					String rs = map.get(newTexts.get(ctx.expr(0)));
					if(rs==null)
						expr +=newTexts.get(ctx.expr(0));
					else
						expr +=analysisResult("lod",rs);
					
					String op=ctx.getChild(1).getText();
					switch (op) {
					case "+":
						op = "add";
						break;
					case "-":
						op = "sub";
						break;
					case "*":
						op = "mult";
						break;
					case "/":
						op = "div";
						break;
					case "%":
						op = "mod";
						break;
					case "==":
						op = "eq";
						break;
					case ">":
						op = "gt";
						break;
					case ">=":
						op = "ge";
						break;
					case "<":
						op = "lt";
						break;
					case "<=":
						op = "le";
						break;
					case "and":
						op = "and";
						break;
					case "or":
						op = "or";
						break;
					}
					rs = map.get(newTexts.get(ctx.expr(1)));
					if(rs==null)
						expr +=newTexts.get(ctx.expr(1));
					else
						expr +=analysisResult("lod",rs);
					expr += "           "+op+" \n";
									
				}
			}
			// IDENT '(' args ')' |  IDENT '[' expr ']'일 경우
			else if(ctx.getChildCount() == 4)
			{
				if(ctx.getChild(1).getText().equals("(")){ // IDENT '(' args ')'
					expr += "           ldp \n";
					expr += newTexts.get(ctx.args());
					expr += "           call "+ctx.getChild(0).getText() + " \n";
				}
				else{ //IDENT '[' expr ']'일 경우 배열 불러올때
					String rs = map.get(ctx.getChild(0).getText());
					if(rs!=null){
						expr +=newTexts.get(ctx.expr(0));
						expr +=analysisResult("lda",rs);
						expr +="           add \n";
						expr +="           ldi \n";
					}
				}
			}
			// IDENT '[' expr ']' '=' expr 배열 원소에 값 삽입
			else
			{
				String rs = map.get(ctx.getChild(0).getText());
				if(rs!=null){
					expr += newTexts.get(ctx.expr(0));
					expr +=analysisResult("lda", rs);
					expr +="           add \n";
					expr += newTexts.get(ctx.expr(1));
					expr +="           sti \n";
				}
			}
			newTexts.put(ctx, expr);
		}
	}

	
	// args	: expr (',' expr)* | ;
	@Override
	public void exitArgs(MiniCParser.ArgsContext ctx) {
		super.exitArgs(ctx);
		String args = "";
		if(ctx.getChildCount() >= 0)
		{
			for(int i=0; i<ctx.getChildCount(); i++)
			{
				if(i % 2 == 0)
					args += newTexts.get(ctx.expr(i/2));	// expr
			}
		}
		newTexts.put(ctx, args);
	}
	private String analysisResult(String inst,String str){
		String var[] =str.split(":");
		return "           "+inst+" "+var[0]+" "+var[1]+" \n";
	}
	private String calFunc(String funcName) {
		int standard = 11;
		int length = funcName.length();
		String result = funcName;
		for(int i = 0 ; i < standard - length;i++)
			result+=" ";
		return result;
	}
}
