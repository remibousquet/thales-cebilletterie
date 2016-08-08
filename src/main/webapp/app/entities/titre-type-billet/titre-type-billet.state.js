(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('titre-type-billet', {
            parent: 'entity',
            url: '/titre-type-billet',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cebilletterieApp.titreTypeBillet.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/titre-type-billet/titre-type-billets.html',
                    controller: 'TitreTypeBilletController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('titreTypeBillet');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('titre-type-billet-detail', {
            parent: 'entity',
            url: '/titre-type-billet/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cebilletterieApp.titreTypeBillet.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/titre-type-billet/titre-type-billet-detail.html',
                    controller: 'TitreTypeBilletDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('titreTypeBillet');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TitreTypeBillet', function($stateParams, TitreTypeBillet) {
                    return TitreTypeBillet.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'titre-type-billet',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('titre-type-billet-detail.edit', {
            parent: 'titre-type-billet-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/titre-type-billet/titre-type-billet-dialog.html',
                    controller: 'TitreTypeBilletDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TitreTypeBillet', function(TitreTypeBillet) {
                            return TitreTypeBillet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('titre-type-billet.new', {
            parent: 'titre-type-billet',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/titre-type-billet/titre-type-billet-dialog.html',
                    controller: 'TitreTypeBilletDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                libelle: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('titre-type-billet', null, { reload: true });
                }, function() {
                    $state.go('titre-type-billet');
                });
            }]
        })
        .state('titre-type-billet.edit', {
            parent: 'titre-type-billet',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/titre-type-billet/titre-type-billet-dialog.html',
                    controller: 'TitreTypeBilletDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TitreTypeBillet', function(TitreTypeBillet) {
                            return TitreTypeBillet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('titre-type-billet', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('titre-type-billet.delete', {
            parent: 'titre-type-billet',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/titre-type-billet/titre-type-billet-delete-dialog.html',
                    controller: 'TitreTypeBilletDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TitreTypeBillet', function(TitreTypeBillet) {
                            return TitreTypeBillet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('titre-type-billet', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
