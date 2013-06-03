// Define any routes for the app
// Note that this app is a single page app, and each partial is routed to using the URL fragment. For example, to select the 'home' route, the URL is http://localhost:8080/spark/#/home
angular.module('spark', ['membersService']).config(
        [ '$routeProvider', function($routeProvider) {
            $routeProvider.
            // if URL fragment is /home, then load the home partial, with the
            // MembersCtrl controller
            when('/home', {
                templateUrl : 'partials/home.html',
                controller : MembersCtrl
            // Add a default route
            }).otherwise({
                redirectTo : '/home'
            });
        } ]);