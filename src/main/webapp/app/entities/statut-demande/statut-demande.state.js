(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('statut-demande', {
            parent: 'entity',
            url: '/statut-demande',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cebilletterieApp.statutDemande.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/statut-demande/statut-demandes.html',
                    controller: 'StatutDemandeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('statutDemande');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('statut-demande-detail', {
            parent: 'entity',
            url: '/statut-demande/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cebilletterieApp.statutDemande.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/statut-demande/statut-demande-detail.html',
                    controller: 'StatutDemandeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('statutDemande');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StatutDemande', function($stateParams, StatutDemande) {
                    return StatutDemande.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'statut-demande',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('statut-demande-detail.edit', {
            parent: 'statut-demande-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/statut-demande/statut-demande-dialog.html',
                    controller: 'StatutDemandeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StatutDemande', function(StatutDemande) {
                            return StatutDemande.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('statut-demande.new', {
            parent: 'statut-demande',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/statut-demande/statut-demande-dialog.html',
                    controller: 'StatutDemandeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('statut-demande', null, { reload: true });
                }, function() {
                    $state.go('statut-demande');
                });
            }]
        })
        .state('statut-demande.edit', {
            parent: 'statut-demande',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/statut-demande/statut-demande-dialog.html',
                    controller: 'StatutDemandeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StatutDemande', function(StatutDemande) {
                            return StatutDemande.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('statut-demande', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('statut-demande.delete', {
            parent: 'statut-demande',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/statut-demande/statut-demande-delete-dialog.html',
                    controller: 'StatutDemandeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StatutDemande', function(StatutDemande) {
                            return StatutDemande.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('statut-demande', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
