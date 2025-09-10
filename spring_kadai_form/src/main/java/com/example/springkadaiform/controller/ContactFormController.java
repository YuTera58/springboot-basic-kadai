package com.example.springkadaiform.controller;

import org.springframework.core.Conventions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springkadaiform.form.ContactForm;


@Controller
public class ContactFormController {
    
    @GetMapping("/form")
    public String Form(Model model) {
    	// すでにインスタンスが存在する場合は行わない
        if (!model.containsAttribute("contactForm")) {
            // ビューにフォームクラスのインスタンスを渡す
            model.addAttribute("contactForm", new ContactForm());
        }
        
        return "contactFormView";
    }
    
    /**
     * @ModelAttribute ・入力チェックとは直接関係ないが、画面の情報を Form が受け取るために指定。
	 * @Validated・・・・これを指定することで POST 時に自動的に入力チェックが実行される。
	 * BindingResult・・・入力チェック結果はこの変数に格納される。
	 * 　　　　　　　　　 必ず Form ⇒ BindingResult の順で引数を指定する事。
     */
    @PostMapping("/confirm")
    public String ValidateForm(RedirectAttributes redirectAttributes,
            @ModelAttribute @Validated ContactForm form, BindingResult result) {


    	// バリデーションエラーがあったら終了
        if (result.hasErrors()) {
            // フォームクラスをビューに受け渡す
            redirectAttributes.addFlashAttribute("contactForm", form);
            // バリデーション結果をビューに受け渡す
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX
                    + Conventions.getVariableName(form), result);

            // contactFormView にリダイレクト
            return "redirect:/form";
        }
        
        // 改行のあるメッセージを表示できるように改行コードを置換する
        String s = form.getMessage();
        form.setMessage(s.replace("\n", "<br>"));
        
        //バリデーション OK の場合のビュー
        return "confirmView";
    }
}