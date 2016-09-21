package com.az24.dao;

import java.util.List;

import com.az24.crawler.model.Article;
import com.az24.crawler.model.City;

public class ArticleSetupTinHotProvinceDAO {
	public static void main(String[] args) {
		ArticleDAO articleDAO = new ArticleDAO();
		List<City> cities = articleDAO.getCity();
		int arrCates[] = {4,2,8,6};
		for (City city : cities) {
			System.out.println("City----->"+city.name);
			for (int i : arrCates) {
				List<Article> articles= articleDAO.getArticleByCity(city.id,i);		
				if(articles!=null)
				articleDAO.setUpTinHotProvince(articles);
			}
			
		}
	}
}
