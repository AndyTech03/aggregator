export default function assert(value, msg=null) {
	if (value == false)
		throw new Error('Assertion error! ' + (msg || ''))
}