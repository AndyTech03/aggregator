import { likesArray, newsArray, viewsArray } from "./faker/news";

export default async function fetchHistory(offset, count, userId) {
	return newsArray
	.map((news) => {
		return {
			...news, 
			views: viewsArray.filter(i => i.newsId == news.id),
			likes: likesArray.filter(i => i.newsId == news.id),
		}
	})
	.filter(i => i.views.find(v => v.userId == userId))
	.slice(offset, offset+count)
}
