import { likesArray, newsArray, viewsArray } from "./faker/news";

export default async function fetchArticle(itemId) {
	const news = newsArray.find(i => i.id == itemId)
	const views = viewsArray.filter(i => i.newsId == itemId)
	const likes = likesArray.filter(i => i.newsId == itemId)
	return { news, views, likes }
}