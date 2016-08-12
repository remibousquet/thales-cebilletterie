(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('demandes', {
            parent: 'membre',
            url: '/demandes',
            data: {
                authorities: ['ROLE_MEMBRE'],
                pageTitle: 'cebilletterieApp.billetterie.demandes.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/billetterie/membre/demandes/demandes.html',
                    controller: 'DemandesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('billetterie');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('demandes.new', {
            parent: 'membre',
            url: '/demandes/new',
            data: {
                authorities: ['ROLE_MEMBRE'],
                pageTitle: 'cebilletterieApp.billetterie.demandes.creation.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/billetterie/membre/demandes/demande-creation.html',
                    controller: 'DemandeCreationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('billetterie');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        });
    }

})();
