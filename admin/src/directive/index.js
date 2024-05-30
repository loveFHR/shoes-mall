import authDirective from './authDirective'
import copyDirective from './copyDirective'

export default (app) => {
	authDirective(app)
	copyDirective(app)
}
