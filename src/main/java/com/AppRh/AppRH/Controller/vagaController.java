package com.AppRh.AppRH.Controller;


import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;//serve para dar a notação de controler a classe
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;//define o endereço do metodo ao realizar a requisição
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.AppRh.AppRH.Model.Candidato;
import com.AppRh.AppRH.Model.Vaga;
import com.AppRh.AppRH.Repository.CandidatoRepository;
import com.AppRh.AppRH.Repository.vagaRepository;

@Controller
public class vagaController {
	
	private vagaRepository vr;
	private CandidatoRepository cr;
	
	//cadastro da vaga
	@RequestMapping(value = "/cadastrarVaga", method = RequestMethod.GET)
	public String form() {
		
		return "vaga/formVaga";
	}
	@RequestMapping(value = "/cadastrarVaga", method = RequestMethod.POST)
	public String form(@Valid Vaga vaga, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique s campos....");
			return "redirect:/cadastrarVaga";
		}
		vr.save(vaga);
		attributes.addFlashAttribute("mensagem", "vaga cadastrada com sucesso");
		return "redirect:/cadastrarVaga";
	}
	
	//Listar vagas
	
	@RequestMapping("/vagas")
	public ModelAndView listavagas() {
		ModelAndView mv = new ModelAndView("vaga/listaVaga");
		Iterable<Vaga> vagas = vr.findAll();
		mv.addObject("vagas", vagas);
		return mv;
	}
	
	//
	@RequestMapping(value="/{codigo}", method = RequestMethod.GET)
	public ModelAndView detalheDaVaga(@PathVariable("codigo") long codigo) {
		Vaga vaga = vr.fingByCodigo(codigo);
		ModelAndView mv = new ModelAndView("vaga/tetalhesvaga");
		mv.addObject("vaga", vaga);
		Iterable<Candidato> candidatos = cr.findByVaga(vaga);
		mv.addObject("candidatos", candidatos);
		
		return mv;
	}
	//Deleta vaga 
	@RequestMapping("/deletarVaga")
	public String deletarVaga(long codigo) {
		Vaga vaga = vr.fingByCodigo(codigo);
		vr.delete(vaga);
		return "redirect:/vagas";
	}
	
	public String detalhesVagaPost(@PathVariable("codigo") long codigo, @Valid Candidato candidato, BindingResult result, RedirectAttributes attributes) {
		
		if(result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "verifique os campos");
			return "redirect:/{codigo}";
		}
		//Rg duplicado
		if(cr.findByRg(candidato.getRG())!= null) {
			attributes.addFlashAttribute("mensagem erro", "RG Duplicado");
			return "redirect:/{codigo}";
		}
		Vaga vaga = vr.fingByCodigo(codigo);
		candidato.setVaga(vaga);
		cr.save(candidato);
		attributes.addFlashAttribute("mensagem", "candidato adicionado com sucesso");
		return "redirect:/{codigo}";
		
	}
	//DELETA CANDIDATO PELO RG
	@RequestMapping("/deletarCandidato")
	public String deletarCandidato(String Rg) {
		Candidato candidato = cr.findByRg(Rg);
		Vaga vaga = candidato.getVaga();
		String codigo =""+ vaga.getCodigo();
		
		cr.delete(candidato);
		return "redirect:/" + codigo;
	}
	
	///Metodos para atualizar vaga 
	//Formulario de edicao de vaga
	@RequestMapping(value = "/editar-vaga", method = RequestMethod.GET)
	public ModelAndView editarVaga(long codigo) {
		Vaga vaga = vr.fingByCodigo(codigo);
		ModelAndView mv = new ModelAndView("vaga/update-vaga");
		mv.addObject("vaga", vaga);
		return mv;
		
	}
	
	//Update vaga
	@RequestMapping(value = "/editar-vaga", method = RequestMethod.POST)
	public string updateVaga(@Valid Vaga vaga, BindingResult result, RedirectAttributes Attributes) {
		vr.save(vaga);
		Attributes.addFlashAttribute("Sucess", "vaga alterada com sucesso");
		long codigoLong = vaga.getCodigo();
		String codigo = "" + codigoLong;
		return "redirect:/" + codigo;
	}
	
	
	
}
