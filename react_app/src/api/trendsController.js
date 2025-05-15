import { apiHost } from "../routes";

const host = `${apiHost}/trends`

export async function getTopNews(offset, limit, date) {
	let dayDate = new Date(date)
	dayDate.setHours(0, 0, 0, 0)
	return fetch(`${host}/getTopNews`, {
		method: 'POST',
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json',
		},
		body: JSON.stringify({
			offset, 
			limit,
			date: dayDate,
		})
	}).then((response) => response.json())
}