package gatewayapi.business.bankaccount;

import dto.bankapiswagger.Type;
import gatewayapi.repository.bankaccount.TypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeBusiness {

    private final TypeRepository typeRepository;

    public TypeBusiness(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    public List<Type> getAllTypes() {
        return typeRepository.getAllTypes();
    }

    public Type getTypeById(Integer id) {
        return typeRepository.getTypeById(id);
    }

    public Type createType(Type type) {
        return typeRepository.createType(type);
    }
}
