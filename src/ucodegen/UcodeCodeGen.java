package ucodegen;

import antlrMiniC.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class UcodeCodeGen {

	public static void main(String[] args) throws Exception{
		minic2ucode("test.c");
	}
	public static void minic2ucode(String code) throws Exception{
		MiniCLexer lexer 			= new MiniCLexer(new ANTLRInputStream(new ByteArrayInputStream(code.getBytes())));
		CommonTokenStream tokens 	= new CommonTokenStream(lexer);
		MiniCParser parser 			= new MiniCParser(tokens);
		ParseTree tree 				= parser.program();
		
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(new UcodeGenListener(), tree);
	}
}
